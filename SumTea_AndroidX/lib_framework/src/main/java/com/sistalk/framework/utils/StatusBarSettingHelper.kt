package com.sistalk.framework.utils

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.sistalk.framework.R

object StatusBarSettingHelper {

    /**
     * 设置状态黑色字体图标，有些页面不需要设置
     * 适配4.4以上MIUI、Flyme和6.0以上其他Android
     * 4.4版本则设置下状态栏的颜色
     * */
    fun statusBarLightMode(activity: Activity) {
        if (!needSetStatusBarLightMode(activity)) return
        statusBarLightMode(activity, true)
    }

    fun statusBarLightMode(activity: Activity,isLightModel:Boolean) {
        val navStatusBar = activity.findViewById<View>(R.id.nav_status_bar)
        navStatusBar?.setBackgroundColor(ContextCompat.getColor(activity,android.R.color.transparent))
        val result:Int = if (isLightModel) {
            StatusBarUtil.setStatusBarLightMode(activity)
        } else {
            StatusBarUtil.setStatusBarDarkMode(activity)
        }
        if (result == StatusBarUtil.STATUS_BAR_TYPE_DEFAULT) {
            if (isLightModel && navStatusBar != null) {
                navStatusBar.setBackgroundColor(ContextCompat.getColor(activity,R.color.status_bar_bg))
            }
            setupStatusBarView(activity,isLightModel)
        }
    }

    /**
     * 设置状态栏透明
     * */
    fun setStatusBarTranslucent(activity: Activity) {
        val window = activity.window
        val decorView = window.decorView as ViewGroup
        val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun setupStatusBarView(mActivity: Activity, isLightModel: Boolean) {
        val window = mActivity.window
        val mDecorView = window.decorView as ViewGroup
        var statusBarView = mDecorView.findViewById<View>(R.id.immersion_status_bar_view)
        if (statusBarView == null) {
            statusBarView = View(mActivity)
            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity = mActivity)
            )
            params.gravity = Gravity.TOP
            statusBarView.layoutParams = params
            statusBarView.visibility = View.VISIBLE
            statusBarView.id = R.id.immersion_status_bar_view
            mDecorView.addView(statusBarView)
        }
        if (isLightModel) {
            statusBarView.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT,Color.BLACK,0.2f))
        } else {
            statusBarView.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT,Color.TRANSPARENT,0.0f))
        }
    }

    private fun needSetStatusBarLightMode(activity: Activity):Boolean {
        return if (activity is StatusBarLightModeInterface) {
            (activity as StatusBarLightModeInterface).needSetStatusBarLightMode()
        } else true
    }


    private fun getStatusBarHeight(activity: Activity):Int {
        return StatusBarUtil.getStatusBarHeight(activity)
    }

    interface StatusBarLightModeInterface {
        // 是否设置日间模式
        fun needSetStatusBarLightMode():Boolean
    }

}