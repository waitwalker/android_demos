package com.sistalk.sumtea_androidx
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.sistalk.framework.helper.SumAppHelper
import com.sistalk.framework.log.LogUtil
import com.sistalk.framework.manager.ActivityManager
import com.sistalk.framework.manager.AppFrontBack
import com.sistalk.framework.manager.AppFrontBackListener
import com.sistalk.framework.manager.AppManager
import com.sistalk.framework.toast.TipsToast
import com.sistalk.starter.dispatcher.TaskDispatcher
import com.sistalk.sumtea_androidx.task.InitARouterTask
import com.sistalk.sumtea_androidx.task.InitAppManagerTask
import com.sistalk.sumtea_androidx.task.InitMMKVTask
import com.sistalk.sumtea_androidx.task.InitRefreshLayoutTask
import com.sistalk.sumtea_androidx.task.InitSumHelperTask
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel


class SumApplication:Application() {

    // 一般用于全局初始化。例如：国际化，主题等
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()

        /// 初始化 App 辅助类，提供application 对象
        SumAppHelper.init(this, BuildConfig.DEBUG)

        /// 初始化 路由
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(SumAppHelper.getApplication())

        /// 初始化腾讯MMKV组件（高效Key Value）
        val rootDir:String = MMKV.initialize(SumAppHelper.getApplication())
        MMKV.setLogLevel(
            if (BuildConfig.DEBUG) {
                MMKVLogLevel.LevelDebug
            } else {
                MMKVLogLevel.LevelError
            }
        )
        LogUtil.d("mmkv root:$rootDir", tag = "MMKV")

        /// 初始化App 管理类
        AppManager.init(SumAppHelper.getApplication())

        /// 初始化 全局下拉刷新 & 上拉加载
        SmartRefreshLayout.setDefaultRefreshHeaderCreator{ context, layout ->
            layout.setPrimaryColorsId(R.color.white)
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator{ context, _ ->
            ClassicsFooter(context)
        }

        // 1.注册app前后台切换监听
        appFrontBackRegister()
        // 2.注册Activity生命周期监听
        registerActivityLifecycle()
        // 3.初始toast
        TipsToast.init(this)

        // 4.初始化任务调度器
//        TaskDispatcher.init(this)
//        val dispatcher:TaskDispatcher = TaskDispatcher.createInstances()
//        dispatcher.addTask(InitSumHelperTask(this))
//            .addTask(InitMMKVTask())
//            .addTask(InitAppManagerTask())
//            .addTask(InitRefreshLayoutTask())
//            .addTask(InitARouterTask())
//            .start()
//        dispatcher.await()
    }

    /*
    * 注册APP前后台切换监听
    * */
    private fun appFrontBackRegister() {
        AppFrontBack.register(this, object : AppFrontBackListener {
            override fun onFront(activity: Activity?) {
                LogUtil.d("onFront")
            }

            override fun onBack(activity: Activity?) {
                LogUtil.d("onBack")
            }

        })
    }

    private fun registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(object :ActivityLifecycleCallbacks{
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityManager.push(activity)
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityManager.pop(activity)
            }
        })
    }
}

