package com.sistalk.lib_framework.manager

import android.app.Activity
import android.app.Application

object AppFrontBack {

    fun register(application: Application, listener:AppFrontBackListener) {

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