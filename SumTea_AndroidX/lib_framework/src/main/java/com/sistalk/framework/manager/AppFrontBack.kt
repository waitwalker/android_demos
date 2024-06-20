package com.sistalk.framework.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle

object AppFrontBack {

    private var activityStartCount = 0

    fun register(application: Application, listener:AppFrontBackListener) {
        application.registerActivityLifecycleCallbacks(object :Application.ActivityLifecycleCallbacks{
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
                activityStartCount++
                if (activityStartCount == 1) {
                    listener.onFront(activity)
                }
            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {
                activityStartCount--
                if (activityStartCount == 0) {
                    listener.onBack(activity)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })
    }
}


interface AppFrontBackListener {

    /*
    * 前台
    * */
    fun onFront(activity: Activity?)

    /*
    * 后台
    * */
    fun onBack(activity: Activity?)
}