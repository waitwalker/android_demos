package com.sistalk.servicedemo

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 继承自Service
class MyService : LifecycleService() {
    private val tag = "Hello"

    private var number = 0
    var numberLiveData = MutableLiveData(0)

    inner class MyBinder : Binder() {
        var service: MyService = this@MyService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        // 绑定之前进行计数
        lifecycleScope.launch {
            while (true) {
                delay(1000)
                numberLiveData.value = numberLiveData.value?.plus(1)
            }
        }
        return MyBinder()
    }

    override fun onCreate() {
        Log.d(tag, "onCreate:Service")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(tag, "onStartCommand:Service")
        lifecycleScope.launch {
            while (true) {
                delay(1000)
                Log.d(tag, "onStartCommand:${number++}")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy:Service")
        super.onDestroy()
    }
}