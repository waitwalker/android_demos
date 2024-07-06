package com.sistalk.framework.manager

import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Resources
import android.provider.Settings
import com.sistalk.framework.log.LogUtil
import com.sistalk.framework.utils.DeviceInfoUtils
import kotlin.math.max


object AppManager {

    private val tag = AppManager::class.java.simpleName
    private lateinit var mContext: Application

    private var mScreenWidthPx = 0
    private var mScreenHeightPx = 0
    private var mScreenWidthDp = 0
    private var mScreenHeightDp = 0

    // 像素密度 每英寸像素个数
    private var mDensityDpi = 0

    // density scale
    private var mDensity = 0f

    private var mStatusBarHeight = 0

    private var mProductType: String? = null

    private var mIsBiggerScreen = false

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun init(application: Application) {
        mContext = application
        val metrics = application.resources.displayMetrics
        mScreenWidthDp = max(metrics.heightPixels, metrics.widthPixels)
        mScreenHeightPx = max(metrics.heightPixels, metrics.widthPixels)
        mIsBiggerScreen = mScreenWidthPx * 1.0 / mScreenWidthPx > 16.0 / 9
        mDensityDpi = metrics.densityDpi
        mDensity = metrics.density
        mScreenWidthDp = (mScreenWidthPx / mDensity).toInt()
        mScreenHeightDp = (mScreenHeightPx / mDensity).toInt()


        val resourceId: Int =
            application.resources.getIdentifier("status_bar_height", "dimen", "android")
        mStatusBarHeight = application.resources.getDimensionPixelSize(resourceId)
    }

    fun getScreenWidthPx(): Int {
        return mScreenWidthPx
    }

    fun getScreenHeightPx(): Int {
        return mScreenHeightPx
    }

    fun getScreenContentHeightPx(): Int {
        return mScreenHeightPx - getStatusBarHeight()
    }

    fun getScreenWidthDp(): Int {
        return mScreenWidthDp
    }

    fun getScreenHeightDp(): Int {
        return mScreenHeightDp
    }

    fun getDensityDpi(): Int {
        return mDensityDpi
    }

    fun getDensity(): Float {
        return mDensity
    }

    private fun getStatusBarHeight(): Int {
        return mStatusBarHeight
    }


    fun getProductType(): String? {
        return mProductType
    }

    fun genProductType(): String {
        val model = DeviceInfoUtils.phoneModel
        return model.replace("[:{} \\[\\]\"']*", "")
    }

    fun getSmartBarHeight(): Int {
        if (isMeizu() && hasSmartBar()) {
            val autoHideSmartBar =
                Settings.System.getInt(mContext.contentResolver, "mz_smartbar_auto_hide", 0) == 1
            return if (autoHideSmartBar) {
                0
            } else {
                getNormalNavigationBarHeight()
            }
        }
        return 0
    }

    @SuppressLint("InternalInsetResource")
    private fun getNormalNavigationBarHeight(): Int {
        try {
            val res: Resources = mContext.resources
            val rid = res.getIdentifier("config_showNavigationBar", "bool", "android")
            if (rid > 0) {
                val flag = res.getBoolean(rid)
                if (flag) {
                    val resourceID = res.getIdentifier("navigation_bar_height", "dimen", "android")
                    if (resourceID > 0) {
                        return res.getDimensionPixelSize(resourceID)
                    }
                }
            }
        } catch (e: Exception) {
            LogUtil.e("getNormalNavigationBarHeight error = ${e.stackTrace}", throwable = e, tag = tag)
        }
        return 0
    }

    private fun isMeizu(): Boolean {
        return DeviceInfoUtils.phoneManufacturer.equals("Meizu", ignoreCase = true)
    }

    private fun hasSmartBar(): Boolean {
        try {
            val method = Class.forName("android.os.Build").getMethod("hasSmartBar")
            return method.invoke(null) as Boolean
        } catch (e: Exception) {
            LogUtil.e("has smart bar error = ${e.stackTrace}", throwable = e, tag = tag)
        }

        if (DeviceInfoUtils.phoneDevice == "mx2") {
            return true
        } else if (DeviceInfoUtils.phoneDevice == "mx" || DeviceInfoUtils.phoneDevice == "m9") {
            return false
        }
        return false
    }

}