package com.sistalk.starter.dispatcher

import com.sistalk.starter.task.Task
import java.util.concurrent.Future

class TaskDispatcher private constructor() {
    private var mStartTime: Long = 0
    private val mFutures:MutableList<Future<*>> = ArrayList()
    private var mAllTasks:MutableList<Task> = ArrayList()
    private val mClsAllTasks:MutableList<Class<out Task>> = ArrayList()

}