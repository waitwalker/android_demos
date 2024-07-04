package com.sistalk.sumtea_androidx.task

import android.app.Application
import androidx.multidex.BuildConfig
import com.sistalk.framework.helper.SumAppHelper
import com.sistalk.framework.log.LogUtil
import com.sistalk.starter.task.Task
import com.sistalk.starter.utils.DispatcherExecutor
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import java.util.concurrent.ExecutorService

/**
 * 初始化辅助类
 * */
class InitSumHelperTask(private val application: Application): Task() {
    override fun needWait(): Boolean {
        return true
    }

    override fun run() {
        SumAppHelper.init(application, BuildConfig.DEBUG)
    }
}

/**
 * 腾讯开源Key-Value组件
 * */
class InitMMKVTask():Task() {
    override fun needWait(): Boolean {
        return true
    }

    /**
     * 指定依赖，依赖完成后才能执行
     * */
    override fun dependsOn(): List<Class<out Task?>?>? {
        val tasks = mutableListOf<Class<out Task?>>()
        tasks.add(InitSumHelperTask::class.java)
        return tasks
    }

    /**
     * 指定线程
     * */
    override fun runOn(): ExecutorService? {
        return DispatcherExecutor.IOExecutor
    }

    override fun run() {
        val rootDir:String = MMKV.initialize(SumAppHelper.getApplication())
        MMKV.setLogLevel(
            if (BuildConfig.DEBUG) {
                MMKVLogLevel.LevelDebug
            } else {
                MMKVLogLevel.LevelError
            }
        )
        LogUtil.d("mmkv root:$rootDir", tag = "MMKV")
    }

}