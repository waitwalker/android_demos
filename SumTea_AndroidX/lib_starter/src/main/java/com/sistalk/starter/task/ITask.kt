package com.sistalk.starter.task

import androidx.annotation.IntRange
import android.os.Process


interface ITask {

    /**
     * 优先级范围，可根据Task重要程度及工作量指定；之后根据实际情况决定是否有必要放更大
     *
     *
     * */
    @IntRange(from = Process.THREAD_PRIORITY_FOREGROUND.toLong(), to = Process.THREAD_PRIORITY_LOWEST.toLong())
    fun priority():Int


}