package com.sistalk.starter.task

import android.content.Context
import com.sistalk.starter.dispatcher.TaskDispatcher

/**
 * Task基类
 * */
abstract class Task : ITask{

    protected var mContext:Context? = context

    protected var mIsMainProcess:Boolean = TaskDispatcher.isMainProcess

    @Volatile
    var isWaiting = false
}