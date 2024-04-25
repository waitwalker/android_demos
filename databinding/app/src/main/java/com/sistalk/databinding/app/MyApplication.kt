package com.sistalk.databinding.app

import android.app.Application
import android.content.Context


open class MyApplication: Application() {

    override fun onCreate() {
        println("applicationContext=$applicationContext")
        super.onCreate()
    }

    fun appContext():Context {
        println("applicationContext=$applicationContext")
        return applicationContext
    }
}