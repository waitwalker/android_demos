package com.sistalk.framework.manager

import android.app.Application
import android.content.Context
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.view.Display
import android.view.WindowManager
import androidx.core.content.getSystemService

object AppManager {

    private val tag = AppManager::class.java.simpleName
    private lateinit var mContext:Application

    private var mScreenWidthPx = 0
    private var mScreenHeightPx = 0
    private var mScreenWidthDp = 0
    private var mScreenHeightDp = 0

    private var mDensityDpi = 0
    private var mDensity = 0

    private var mStatusBarHeight = 0

    private var mProductType:String? = null

    private var mIsBiggerScreen = false

    fun init(application: Application) {
        mContext = application
        val windowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayManager:DisplayManager = application.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val defaultDisplay = displayManager.getDisplay(Display.DEFAULT_DISPLAY)
        val point = Point()


    }

}