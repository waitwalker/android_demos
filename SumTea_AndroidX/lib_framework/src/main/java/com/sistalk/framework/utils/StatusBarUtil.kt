package com.sistalk.framework.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.sistalk.framework.log.LogUtil

/**
 * 状态栏工具类
 * */
object StatusBarUtil {
    const val STATUS_BAR_TYPE_DEFAULT = 0
    const val STATUS_BAR_TYPE_MI_UI = 1
    const val STATUS_BAR_TYPE_FLY_ME = 2
    const val STATUS_BAR_TYPE_ANDROID_M = 3
    const val STATUS_BAR_TYPE_OPP = 4
    const val SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010

    fun setStatusBarLightMode(activity: Activity?):Int {
        var result = STATUS_BAR_TYPE_DEFAULT
        if (activity != null) {
            result = STATUS_BAR_TYPE_ANDROID_M
        }
        return result
    }


    @SuppressLint("PrivateApi")
    private fun setStatusBarModeForMIUI(window: Window?, darkText:Boolean):Boolean {
        var result = false
        if (window != null) {
            val clazz:Class<*> = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags",Int::class.javaPrimitiveType,Int::class.javaPrimitiveType)
                if (darkText) {
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag)//状态栏透明黑色字体
                } else {
                    extraFlagField.invoke(window,0,darkModeFlag) //清除黑色字体
                }
                var vis = window.decorView.systemUiVisibility
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                vis = if (darkText) {
                    vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
                window.decorView.systemUiVisibility = vis
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                result = true
            } catch (e:Exception) {
                LogUtil.e("MIUI status bar set error = $e")
            }
        }

        return result
    }
}