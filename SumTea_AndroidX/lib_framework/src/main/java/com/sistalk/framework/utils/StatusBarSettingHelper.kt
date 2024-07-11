package com.sistalk.framework.utils

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import com.sistalk.framework.R

object StatusBarSettingHelper {

    /**
     * 设置状态黑色字体图标，有些页面不需要设置
     * 适配4.4以上MIUI、Flyme和6.0以上其他Android
     * 4.4版本则设置下状态栏的颜色
     * */
    fun statusBarLightMode(activity: Activity) {

    }

    fun statusBarLightMode(activity: Activity,isLightModel:Boolean) {
        val navStatusBar = activity.findViewById<View>(R.id.nav_status_bar)
        navStatusBar?.setBackgroundColor(ContextCompat.getColor(activity,android.R.color.transparent))
        val result:Int = if (isLightModel) {

        }
    }

}