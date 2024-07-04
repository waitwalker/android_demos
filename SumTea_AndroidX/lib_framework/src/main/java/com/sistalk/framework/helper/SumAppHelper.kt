package com.sistalk.framework.helper

import android.app.Application

object SumAppHelper {
    private lateinit var app:Application
    private var isDebug = false
    fun init(application: Application, isDebug:Boolean) {
        this.app = application
        this.isDebug = isDebug
    }

    /**
     * 获取全局application
     * */
    fun getApplication() = app

    fun isDebug() = isDebug
}