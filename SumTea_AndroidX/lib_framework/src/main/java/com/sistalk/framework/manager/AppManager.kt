package com.sistalk.framework.manager

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display
import kotlin.math.max


object AppManager {

    private val tag = AppManager::class.java.simpleName
    private lateinit var mContext:Application

    private var mScreenWidthPx = 0
    private var mScreenHeightPx = 0
    private var mScreenWidthDp = 0
    private var mScreenHeightDp = 0

    // 像素密度 每英寸像素个数
    private var mDensityDpi = 0
    // density scale
    private var mDensity = 0f

    private var mStatusBarHeight = 0

    private var mProductType:String? = null

    private var mIsBiggerScreen = false

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun init(application: Application) {
        mContext = application
        val displayManager:DisplayManager = application.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val defaultDisplay = displayManager.getDisplay(Display.DEFAULT_DISPLAY)
        val metrics = application.resources.displayMetrics
        mScreenWidthDp = max(metrics.heightPixels, metrics.widthPixels)
        mScreenHeightPx = max(metrics.heightPixels, metrics.widthPixels)
        mIsBiggerScreen = mScreenWidthPx * 1.0 / mScreenWidthPx > 16.0 / 9
        mDensityDpi = metrics.densityDpi
        mDensity = metrics.density
        mScreenWidthDp = (mScreenWidthPx / mDensity).toInt()
        mScreenHeightDp = (mScreenHeightPx / mDensity).toInt()


        val resourceId: Int = application.resources.getIdentifier("status_bar_height", "dimen", "android")
        mStatusBarHeight = application.resources.getDimensionPixelSize(resourceId)
    }

    fun getScreenWidthPx():Int {
        return mScreenWidthPx
    }

    fun getScreenHeightPx():Int {
        return mScreenHeightPx
    }

    fun getScreenContentHeightPx():Int {
        return  mScreenHeightPx - getStatusBarHeight()
    }

    fun getScreenWidthDp():Int {
        return mScreenWidthDp
    }

    fun getScreenHeightDp():Int {
        return mScreenHeightDp
    }

    fun getDensityDpi():Int {
        return mDensityDpi
    }

    fun getDensity():Float {
        return mDensity
    }

    fun getStatusBarHeight():Int {
        return mStatusBarHeight
    }


    fun getProductType():String? {
        return mProductType
    }

    fun fun genProductType():String {
        val model = DeviceIn
    }

}