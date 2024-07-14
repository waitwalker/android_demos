package com.sistalk.framework.utils

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//import com.sistalk.framework.R
import com.sistalk.framework.log.LogUtil



/**
 * 状态栏工具类
 * */
object StatusBarUtil {
    const val tag = "StatusBarUtil"
    const val STATUS_BAR_TYPE_DEFAULT = 0
    const val STATUS_BAR_TYPE_MI_UI = 1
    const val STATUS_BAR_TYPE_FLY_ME = 2
    const val STATUS_BAR_TYPE_ANDROID_M = 3
    const val STATUS_BAR_TYPE_OPP = 4
    const val SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010

    /**
     * 设置日间模式下状态栏
     * */
    fun setStatusBarLightMode(activity: Activity?): Int {
        var result = STATUS_BAR_TYPE_DEFAULT
        if (activity != null) {
            result = STATUS_BAR_TYPE_ANDROID_M
            if (setStatusBarModeForAndroidM(activity.window, true)) {
                result = STATUS_BAR_TYPE_ANDROID_M
            }

            if (setStatusBarModeForMIUI(activity.window, true)) {
                result = STATUS_BAR_TYPE_MI_UI
            } else if (setStatusBarForFlyMe(activity.window, darkText = true)) {
                result = STATUS_BAR_TYPE_FLY_ME
            }
        }

        return result
    }

    /**
     * 设置暗黑模式下状态栏
     * */
    fun setStatusBarDarkMode(activity: Activity?): Int {
        var result = STATUS_BAR_TYPE_DEFAULT
        if (activity != null) {
            if (setStatusBarModeForAndroidM(activity.window, false)) {
                result = STATUS_BAR_TYPE_ANDROID_M
            }

            if (setStatusBarModeForMIUI(activity.window, false)) {
                result = STATUS_BAR_TYPE_MI_UI
            } else if (setStatusBarForFlyMe(activity.window, false)) {
                result = STATUS_BAR_TYPE_FLY_ME
            }
        }
        return result
    }

    @SuppressLint("PrivateApi")
    private fun setStatusBarForFlyMe(window: Window?, darkText: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag =
                    WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meiZuFlags =
                    WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meiZuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meiZuFlags.getInt(lp)
                value = if (darkText) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meiZuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {
                LogUtil.e("setStatusBarForFlyMe error = $e", tag = tag)
            }
        }
        return result
    }


    @SuppressLint("PrivateApi")
    private fun setStatusBarModeForMIUI(window: Window?, darkText: Boolean): Boolean {
        var result = false
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (darkText) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
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
            } catch (e: Exception) {
                LogUtil.e("MIUI status bar set error = $e")
            }
        }

        return result
    }

    /**
     * 设置原生Android 6.0以上系统状态栏
     * */
    private fun setStatusBarModeForAndroidM(window: Window?, darkText: Boolean): Boolean {
        var result = false
        if (window != null) {
            window.decorView.systemUiVisibility =
                if (darkText) View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or 0x0000200 else View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_VISIBLE
            result = true
        }
        return result
    }

    fun setStatusBarModeForOpp(window: Window, darkText: Boolean):Boolean {
        var result = false
        try {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            var vis = window.decorView.systemUiVisibility
            vis =
                if (darkText) {
                    vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
            window.decorView.systemUiVisibility = vis
            result = true
        } catch (e:Exception) {
            LogUtil.e("setStatusBarModeForOpp error = $e", tag = tag)
        }
        return result
    }

    /**
     * 设置状态栏颜色
     * */
    fun setColor(activity: Activity, @ColorInt color:Int, statusBarAlpha:Int) {
        // 先设置全屏
        setFullScreen(activity)
        // 再透明状态栏的垂直下方放置一个和状态栏同样高度的View
        addStatusBarBehind(activity, color, statusBarAlpha)
    }

    private fun addStatusBarBehind(activity: Activity,@ColorInt color: Int,statusBarAlpha: Int) {
        // 获取window 下的decorView
        val decorView = activity.window.decorView as ViewGroup
        val count = decorView.childCount
        // 判断是否已经添加了statusBarView
        if (count > 0 && decorView.getChildAt(count - 1) is StatusBarView) {
            decorView.getChildAt(count - 1).setBackgroundColor(calculateStatusColor(color,statusBarAlpha))
        } else {
            val statusBarView = createStatusBarView(activity,color,statusBarAlpha)
            decorView.addView(statusBarView)
        }
        setRootView(activity)
    }

    fun setTranslucentImageHeader(activity: Activity,alpha: Int,needOffsetView:View?,intArray: IntArray = intArrayOf(0,0,0),hasOffset:Boolean = true) {
        setFullScreen(activity)
        val decorView = activity.window.decorView as ViewGroup
        val count = decorView.childCount
        if (count > 0 && decorView.getChildAt(count-1) is StatusBarView) {
            decorView.getChildAt(count -1).setBackgroundColor(Color.argb(alpha,intArray[0],intArray[1], intArray[2]))
        } else {
            val statusView = createStatusBarView(activity,alpha)
        }
    }

    fun hideStatusBarView(activity: Activity) {
        val decorView = activity.window.decorView as ViewGroup
        val count = decorView.childCount
        if (count > 0 && decorView.getChildAt(count -1) is StatusBarView) {
            decorView.getChildAt(count - 1).gone()
        }
    }

    /**
     * 获取状态栏高度
     * */
    private fun getStatusBarHeight(activity: Activity):Int {
        var statusBarHeight = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.decorView.setOnApplyWindowInsetsListener{ _, insets ->
                statusBarHeight = insets.getInsets(WindowInsets.Type.statusBars()).top
                insets
            }
            activity.window.decorView.requestApplyInsets()
        } else {
            val rootView = (activity.findViewById<View>(R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
            ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
                statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                insets
            }
        }
        return statusBarHeight
    }

    private fun calculateStatusColor(color: Int,alpha:Int):Int {
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }

    private fun setFullScreen(activity: Activity) {
        val window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        window.statusBarColor = Color.TRANSPARENT
    }


    private fun setRootView(activity: Activity) {
        val rootView = (activity.findViewById<View>(R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        ViewCompat.setFitsSystemWindows(rootView,true)
        rootView.clipToPadding = true
    }

    private fun createStatusBarView(activity: Activity,color: Int,alpha: Int):StatusBarView {
        val statusBarView = StatusBarView(activity)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(activity)
        )
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(calculateStatusColor(color,alpha))
        return statusBarView
    }

    class StatusBarView:View {
        constructor(context: Context?):super(context)
        constructor(context: Context?, attrs:AttributeSet?):super(context,attrs)
        constructor(context: Context?,attrs: AttributeSet?,defStyleAttr:Int) : super(context, attrs, defStyleAttr)

        }
}