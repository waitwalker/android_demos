package com.sistalk.starter

import com.sistalk.framework.log.LogUtil
import java.util.concurrent.atomic.AtomicInteger

/**
 * 任务开始
 * */
object TaskStart {

    @Volatile
    private var sCurrentSituation = ""
    private val sBeans:MutableList<TaskStartBean> = ArrayList()
    private var sTaskDoneCount = AtomicInteger()
    private const val sOpenLaunchStart = true //是否开启统计
    var currentSituation:String
        get() = sCurrentSituation
        set(value) {
            if (!sOpenLaunchStart) return
            LogUtil.d("currentSituation $currentSituation")
            sCurrentSituation = value
            setLaunchStart()
        }

    fun markTaskDone() {
        sTaskDoneCount.getAndDecrement()
    }

    fun setLaunchStart() {
        val bean = TaskStartBean()
        bean.situation = sCurrentSituation
        bean.count = sTaskDoneCount.get()
        sBeans.add(bean)
        sTaskDoneCount = AtomicInteger(0)
    }
}