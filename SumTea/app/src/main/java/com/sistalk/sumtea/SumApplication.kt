package com.sistalk.sumtea

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.sistalk.lib_framework.manager.AppFrontBack
import com.sistalk.lib_framework.manager.AppFrontBackListener


class SumApplication:Application() {

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

            }

            override fun onBack(activity: Activity?) {

            }

        })
    }
}

