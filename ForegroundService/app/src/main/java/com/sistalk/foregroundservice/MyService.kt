package com.sistalk.foregroundservice

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val CHANNEL_ID = "my notification channel ID"

class MyService : LifecycleService() {

    private var number = 0

    @SuppressLint("ForegroundServiceType", "UnspecifiedImmutableFlag")
    override fun onCreate() {
        super.onCreate()
        lifecycleScope.launch {
            while (true) {
                delay(1000)
                Log.d("Hello", "onCreate:${number++}")
            }
        }

        createChannel()
        // 点击通知后跳转到指定activity
        val pendingIntent =
            PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0)
        // 8.0以后必须创建channel，并且channel是用户可以在设置中可以看见&关闭的
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentTitle("This start")
            .setContentTitle("content")
            .setContentIntent(pendingIntent)
            .build()
        // 启动前台服务
        startForeground(1, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "KaiSi"
            val desc = "通知desc"
            // 通知级别
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = desc
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

        }
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        TODO("Return the communication channel to the service.")
    }
}