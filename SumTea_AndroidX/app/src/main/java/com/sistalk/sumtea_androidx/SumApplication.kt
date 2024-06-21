package com.sistalk.sumtea_androidx
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.sistalk.framework.log.LogUtil
import com.sistalk.framework.manager.AppFrontBack
import com.sistalk.framework.manager.AppFrontBackListener


class SumApplication:Application() {

    val tag = "SumApplication"

    // 一般用于全局初始化。例如：国际化，主题等
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()

        appFrontBackRegister()
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

            }
        })
    }
}

