package com.sistalk.starter.task

import android.annotation.SuppressLint
import android.os.Looper
import android.os.Process
import android.os.Trace
import com.sistalk.framework.log.LogUtil
import com.sistalk.starter.TaskStart
import com.sistalk.starter.dispatcher.TaskDispatcher

class DispatchRunnable:Runnable {

    private var mTask:Task
    private var mTaskDispatcher:TaskDispatcher? = null
    constructor(task: Task) {
        mTask = task
    }

    constructor(task: Task, dispatcher: TaskDispatcher) {
        mTask = task
        mTaskDispatcher = dispatcher
    }

    @SuppressLint("UnclosedTrace")
    override fun run() {
        Trace.beginSection(mTask.javaClass.simpleName)
        LogUtil.d("${mTask.javaClass.simpleName}开始执行 | Situation:${TaskStart.currentSituation}")
        Process.setThreadPriority(mTask.priority())
        var startTime = System.currentTimeMillis()
        mTask.isWaiting = true
        mTask.waitToSatisfy()
        val waitTime = System.currentTimeMillis() - startTime
        startTime = System.currentTimeMillis()

        // 执行Task
        mTask.isRunning = true
        mTask.run()

        // 执行Task的尾部任务
        val tailRunnable = mTask.tailRunnable
        tailRunnable?.run()
        if (!mTask.needCall() || !mTask.runOnMainThread()) {
            printTask(startTime, waitTime)
            TaskStart.markTaskDone()
            mTask.isFinished = true
            mTaskDispatcher?.let {
                it.satisfyChildren(mTask)
                it.markTaskDone(mTask)
            }
            LogUtil.d("${mTask.javaClass.simpleName} finish")
        }
        Trace.endSection()
    }

    private fun printTask(startTime:Long, waitTime:Long) {
        val runtime = System.currentTimeMillis() - startTime
        LogUtil.d(
            mTask.javaClass.simpleName + "| wait：" + waitTime + "| run："
                    + runtime + "| isMain：" + (Looper.getMainLooper() == Looper.myLooper())
                    + "| needWait：" + (mTask.needWait() || Looper.getMainLooper() == Looper.myLooper())
                    + "| ThreadId：" + Thread.currentThread().id
                    + "| ThreadName：" + Thread.currentThread().name
                    + "| Situation：" + TaskStart.currentSituation
        )
    }


}