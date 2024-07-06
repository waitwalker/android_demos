package com.sistalk.starter.dispatcher
import android.app.Application
import android.os.Looper
import androidx.annotation.UiThread
import com.sistalk.framework.log.LogUtil
import com.sistalk.starter.TaskStart
import com.sistalk.starter.sort.TaskSortUtil
import com.sistalk.starter.task.DispatchRunnable
import com.sistalk.starter.task.Task
import com.sistalk.starter.task.TaskCallBack
import com.sistalk.starter.utils.StarterUtils
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * 任务调度器
 * */
class TaskDispatcher private constructor() {
    private val tag = "TaskDispatcher"
    private var mStartTime: Long = 0
    private val mFutures: MutableList<Future<*>> = ArrayList()
    private var mAllTasks: MutableList<Task> = ArrayList()
    private val mClsAllTasks: MutableList<Class<out Task>> = ArrayList()

    @Volatile
    private var mMainThreadTasks: MutableList<Task> = ArrayList()
    private var mCountDownLatch: CountDownLatch? = null

    // 需要Wait的Task数量
    private val mNeedWaitCount = AtomicInteger()

    // 调用了await的时候还没结束的且需要等待的Task
    private val mNeedWaitTasks: MutableList<Task> = ArrayList()

    // 已经结束了的Task
    @Volatile
    private var mFinishedTasks: MutableList<Class<out Task>> = ArrayList(100)
    private val mDependedHashMap = HashMap<Class<out Task>, ArrayList<Task>?>()

    // 启动器分析的次数，用于统计分析耗时
    private val mAnalyseCount = AtomicInteger()


    fun addTask(task: Task?): TaskDispatcher {
        task?.let {
            collectionDepends(it)
            mAllTasks.add(it)
            mClsAllTasks.add(it.javaClass)
            // 非主线程且需要wait的，主线程不需要CountDownLatch也是同步的
            if (ifNeedWait(it)) {
                mNeedWaitTasks.add(it)
                mNeedWaitCount.getAndIncrement()
            }
        }
        return this
    }

    private fun collectionDepends(task: Task) {
        task.dependsOn()?.let { list ->
            for (cls in list) {
                cls?.let { clsx ->
                    if (mDependedHashMap[clsx] == null) {
                        mDependedHashMap[clsx] = ArrayList()
                    }
                    mDependedHashMap[clsx]?.add(task)
                    if (mFinishedTasks.contains(clsx)) {
                        task.satisfy()
                    }
                }
            }
        }
    }

    private fun ifNeedWait(task: Task): Boolean {
        return !task.runOnMainThread() && task.needWait()
    }

    @UiThread
    fun start() {
        mStartTime = System.currentTimeMillis()
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw RuntimeException("must be called from UiThread")
        }

        if (mAllTasks.isNotEmpty()) {
            mAnalyseCount.getAndDecrement()
            printDependedMsg()
            mAllTasks = TaskSortUtil.getSortResult(mAllTasks, mClsAllTasks).toMutableList()
            mCountDownLatch = CountDownLatch(mNeedWaitCount.get())
            sendAndExecuteAsyncTasks()
            LogUtil.d(
                "task analyse cost ${System.currentTimeMillis() - mStartTime} begin main",
                tag = tag
            )
            executeTaskMain()
        }
        LogUtil.d(
            "task analyse cost start time cost ${System.currentTimeMillis() - mStartTime}",
            tag = tag
        )
    }

    fun cancel() {
        for (future in mFutures) {
            future.cancel(true)
        }
    }

    private fun executeTaskMain() {
        mStartTime = System.currentTimeMillis()
        for (task in mMainThreadTasks) {
            val time = System.currentTimeMillis()
            DispatchRunnable(task, this).run()
            LogUtil.d("real main ${task.javaClass.simpleName} cost ${System.currentTimeMillis() - time}")
        }
        LogUtil.d("main task cost ${System.currentTimeMillis() - mStartTime}")
    }

    private fun sendAndExecuteAsyncTasks() {
        for (task in mAllTasks) {
            if (task.onlyInMainProcess() && !isMainProcess) {
                markTaskDone(task)
            } else {
                sendTaskReal(task)
            }
            task.isSend = true
        }
    }

    /**
     * 查看被依赖的信息
     * */
    private fun printDependedMsg() {
        LogUtil.i("needWait size : ${mNeedWaitCount.get()}", tag = tag)
        for (cls in mDependedHashMap.keys) {
            LogUtil.i("cls : ${cls.simpleName} ${mDependedHashMap[cls]?.size}", tag = tag)
            mDependedHashMap[cls]?.let {
                for (task in it) {
                    LogUtil.i("cls:${task.javaClass.simpleName}", tag = tag)
                }
            }
        }
    }

    /**
     * 通知Children一个前置任务已经完成
     * */
    fun satisfyChildren(launchTask: Task) {
        val arrayList = mDependedHashMap[launchTask.javaClass]
        if (!arrayList.isNullOrEmpty()) {
            for (task in arrayList) {
                task.satisfy()
            }
        }
    }

    fun markTaskDone(task: Task) {
        if (ifNeedWait(task)) {
            mFinishedTasks.add(task.javaClass)
            mNeedWaitTasks.remove(task)
            mCountDownLatch?.countDown()
            mNeedWaitCount.getAndDecrement()
        }
    }

    private fun sendTaskReal(task: Task) {
        if (task.runOnMainThread()) {
            mMainThreadTasks.add(task)
            if (task.needCall()) {
                task.setTaskCallBack(object : TaskCallBack {
                    override fun call() {
                        TaskStart.markTaskDone()
                        task.isFinished = true
                        satisfyChildren(task)
                        markTaskDone(task)
                        LogUtil.d("${task.javaClass.simpleName} finish", tag = tag)
                    }
                })
            } else {
                // 直接发，是否执行取决于具体线程池
                val future = task.runOn()?.submit(DispatchRunnable(task, this))
                future?.let {
                    mFutures.add(it)
                }
            }
        }
    }

    fun executeTask(task: Task) {
        if (ifNeedWait(task)) {
            mNeedWaitCount.getAndIncrement()
        }
        task.runOn()?.execute(DispatchRunnable(task, this))
    }

    @UiThread
    fun await() {
        try {
            LogUtil.d("still has ${mNeedWaitCount.get()}", tag = tag)
            for (task in mNeedWaitTasks) {
                LogUtil.d("need wait: ${task.javaClass.simpleName}", tag = tag)
            }
            if (mNeedWaitCount.get() > 0) {
                mCountDownLatch?.await(WAIT_TIME.toLong(), TimeUnit.MILLISECONDS)
            }
        } catch (e: InterruptedException) {
            LogUtil.e("await() error = ${e.stackTrace}", tag = tag)
        }
    }


    companion object {
        private const val WAIT_TIME = 10000
        var context: Application? = null
            private set
        var isMainProcess = false
            private set

        @Volatile
        private var sHasInit = false

        fun init(context: Application?) {
            context?.let {
                Companion.context = context
                sHasInit = true
                isMainProcess = StarterUtils.isMainProcess(context)
            }
        }

        /**
         * 每次获取的都是新对象
         * */
        fun createInstances(): TaskDispatcher {
            if (!sHasInit) {
                throw RuntimeException("must call TaskDispatched.init first")
            }
            return TaskDispatcher()
        }
    }
}