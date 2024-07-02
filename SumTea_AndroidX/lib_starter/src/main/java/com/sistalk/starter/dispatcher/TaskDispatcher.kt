package com.sistalk.starter.dispatcher

import android.app.Application
import android.content.Context
import com.sistalk.starter.task.Task
import com.sistalk.starter.utils.StarterUtils
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicInteger

class TaskDispatcher private constructor() {
    private var mStartTime: Long = 0
    private val mFutures:MutableList<Future<*>> = ArrayList()
    private var mAllTasks:MutableList<Task> = ArrayList()
    private val mClsAllTasks:MutableList<Class<out Task>> = ArrayList()

    @Volatile
    private var mMainThreadTasks:MutableList<Task> = ArrayList()
    private var mCountDownLatch:CountDownLatch? = null

    // 需要Wait的Task数量
    private val mNeedWaitCount = AtomicInteger()

    // 调用了await的时候还没结束的且需要等待的Task
    private val mNeedWaitTasks:MutableList<Task> = ArrayList()

    // 已经结束了的Task
    @Volatile
    private var mFinishedTasks:MutableList<Class<out Task>> = ArrayList(100)
    private val mDependedHashMap = HashMap<Class<out Task>, ArrayList<Task>?>()

    // 启动器分析的次数，用于统计分析耗时
    private val mAnalyseCount = AtomicInteger()


    companion object {
        private const val WAIT_TIME = 10000
        var context:Application? = null
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

        fun createInstances():TaskDispatcher {
            if (!sHasInit) {
                throw RuntimeException("must call TaskDispatched.init first")
            }
            return TaskDispatcher()
        }

    }

}