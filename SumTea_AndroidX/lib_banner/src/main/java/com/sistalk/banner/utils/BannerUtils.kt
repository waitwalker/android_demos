package com.sistalk.banner.utils

import android.content.res.Resources
import android.util.Log
import com.sistalk.banner.base.BaseBannerAdapter.Companion.MAX_VALUE


object BannerUtils {

    private var debugMode = false
    private val TAG = "BVP"

    fun setDebugMode(isDebug: Boolean) {
        debugMode = isDebug
    }

    fun isDebugMode(): Boolean {
        return debugMode
    }

    fun dp2px(dpValue: Float): Int {
        return (0.5f + dpValue * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun log(tag: String?, msg: String?) {
        if (isDebugMode()) {
            Log.e(tag, msg ?: "")
        }
    }

    fun log(msg: String?) {
        if (isDebugMode()) {
            log(TAG, msg)
        }
    }

    fun getRealPosition(position: Int, pageSize: Int): Int {
        return if (pageSize == 0) {
            0
        } else (position + pageSize) % pageSize
    }

    fun getOriginalPosition(pageSize: Int): Int {
        return MAX_VALUE / 2 - MAX_VALUE / 2 % pageSize
    }
}