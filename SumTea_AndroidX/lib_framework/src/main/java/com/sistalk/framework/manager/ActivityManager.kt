package com.sistalk.framework.manager

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

/*
* Activity 容器管理
* */
object ActivityManager {
    private val tasks = mutableListOf<Activity>()

    fun push(activity: Activity) {
        tasks.add(activity)
    }

    fun pop(activity: Activity) {
        tasks.remove(activity)
    }

    fun top():Activity? {
        if (tasks.isNotEmpty()) return tasks.last()
        return null
    }

    /*
    *
    * 关闭其他Activity
    * */
    fun finishOtherActivity(clazz: Class<out Activity>) {
        val it = tasks.iterator()
        while (it.hasNext()) {
            val item = it.next()
            if (item::class.java != clazz) {
                it.remove()
                item.finish()
            }
        }
    }

    /*
    * 关闭Activity
    *
    * */
    fun finishActivity(clazz: Class<out Activity>) {
        val it = tasks.iterator()
        while (it.hasNext()) {
            val item = it.next()
            if (item::class.java == clazz) {
                it.remove()
                item.finish()
                break
            }
        }
    }

    /*
    * Activity是否在栈中
    * */
    fun isActivityExistsInStack(clazz: Class<out Activity>?) :Boolean {
        if (clazz != null) {
            for (task in tasks) {
                if (task::class.java == clazz) {
                    return true
                }
            }
        }
        return false
    }
    /*
    * Activity是否销毁
    * */
    fun isActivityDestroy(context: Context) : Boolean {
        val activity = findActivity(context)
        return if (activity != null) {
            activity.isFinishing
        } else true
    }

    private fun findActivity(context: Context):Activity? {
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return findActivity(context.baseContext)
        }
        return null
    }


}