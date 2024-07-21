package com.sistalk.sumtea_androidx.task

import android.app.Application
import androidx.multidex.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
//import com.alibaba.android.arouter.launcher.ARouter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.sistalk.framework.helper.SumAppHelper
import com.sistalk.framework.log.LogUtil
import com.sistalk.framework.manager.AppManager
import com.sistalk.starter.task.Task
import com.sistalk.starter.utils.DispatcherExecutor
import com.sistalk.sumtea_androidx.R
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

class InitAppManagerTask():Task() {

    override fun needWait(): Boolean {
        return true
    }

    override fun dependsOn(): List<Class<out Task?>?>? {
        val tasks = mutableListOf<Class<out Task?>>()
        tasks.add(InitSumHelperTask::class.java)
        return tasks
    }

    override fun run() {
        AppManager.init(SumAppHelper.getApplication())
    }
}


/**
 * 全局刷新配置
 * */
class InitRefreshLayoutTask():Task() {

    override fun needWait(): Boolean {
        return true
    }

    override fun run() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator{ context, layout ->
            layout.setPrimaryColorsId(R.color.white)
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator{ context, layout ->
            ClassicsFooter(context)
        }
    }
}

/**
 * 初始化路由
 * */
class InitARouterTask():Task() {
    override fun needWait(): Boolean {
        return false
    }

    override fun dependsOn(): List<Class<out Task?>?>? {
        val tasks = mutableListOf<Class<out Task?>>()
        tasks.add(InitSumHelperTask::class.java)
        return tasks
    }

    override fun run() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(SumAppHelper.getApplication())
    }
}