package com.sistalk.starter.task
import android.content.Context
import android.os.Process
import com.sistalk.framework.log.LogUtil
import com.sistalk.starter.dispatcher.TaskDispatcher
import com.sistalk.starter.dispatcher.TaskDispatcher.Companion.context
import com.sistalk.starter.utils.DispatcherExecutor
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService

/**
 * Task基类
 * */
abstract class Task : ITask{

    val tag = "Task"

    protected var mContext:Context? = context

    // 当前进程是否主进程
    protected var mIsMainProcess:Boolean = TaskDispatcher.isMainProcess

    // 是否正在等待
    @Volatile
    var isWaiting = false

    // 是否正在执行
    @Volatile
    var isRunning = false

    // Task是否执行完成
    @Volatile
    var isFinished = false

    // Task是否已经被分发
    @Volatile
    var isSend = false

    // 当前Task所依赖的Task数量（需要等地啊倍依赖的Task执行完毕才能执行自己），默认没有依赖
    @Suppress("LeakingThis")
    private val mDepends = CountDownLatch(dependsOn()?.size ?:0)

    /**
     * 当前Task等待，让依赖的Task先执行
     * */
    fun waitToSatisfy() {
        try {
            mDepends.await()
        } catch (e:InterruptedException) {
            LogUtil.e("waitToSatisfy error = ${e.stackTrace}", tag = tag)
        }
    }

    /**
     * 依赖的Task执行完一个
     *
     * */
    fun satisfy() {
        mDepends.countDown()
    }

    /**
     * 是否需要尽快执行，解决特殊场景问题：一个Task耗时非常多但是优先级一般，很有可能开始的时间较晚
     * 导致最后只是在等他，这种可以早开始
     * */
    fun needRunAsSoon():Boolean {
        return false
    }

    /**
     * Task的优先级，运行在主线程则不要去改优先级
     * */
    override fun priority(): Int {
        return Process.THREAD_PRIORITY_BACKGROUND
    }

    /**
     * Task执行在哪个线程池，默认在IO的线程池
     * CPU密集型的一定要切换到DispatcherExecutor.getCPUExecutor()
     * */
    override fun runOn(): ExecutorService? {
        return DispatcherExecutor.IOExecutor
    }

    /**
     * 异步线程执行的Task是否需要在被调用await的时候等待，默认不需要
     * */
    override fun needWait(): Boolean {
        return false
    }

    /**
     * 当前Task依赖的Task集合（需要等待被依赖的Task执行完毕才能执行自己），默认没有依赖
     * */
    override fun dependsOn(): List<Class<out Task?>?>? {
        return null
    }

    /**
     * Task是否运行在主线程，默认false
     * */
    override fun runOnMainThread(): Boolean {
        return false
    }

    override val tailRunnable: Runnable?
        get() = null

    override fun setTaskCallBack(callBack: TaskCallBack?) {

    }

    override fun needCall(): Boolean {
        return false
    }

    /**
     * 是否只在主进程，默认是
     * */
    override fun onlyInMainProcess(): Boolean {
        return true
    }
}

/**
 * 主线程任务
 * */
abstract class MainTask:Task() {
    override fun runOnMainThread(): Boolean {
        return true
    }
}