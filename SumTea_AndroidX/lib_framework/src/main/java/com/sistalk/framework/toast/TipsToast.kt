@file:JvmName("TipToast")

package com.sistalk.framework.toast

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.sistalk.framework.R
import com.sistalk.framework.databinding.WidgetTipsToastBinding
import com.sistalk.framework.log.LogUtil

@SuppressLint("StaticFieldLeak")
object TipsToast {

    private var toast: Toast? = null
    private lateinit var mContext: Context
    private val mToastHandler = Looper.myLooper()?.let { Handler(it) }
    private val mBinding by lazy {
        WidgetTipsToastBinding.inflate(LayoutInflater.from(mContext), null, false)
    }

    fun init(context: Application) {
        mContext = context
    }

    fun showTips(@StringRes stringID:Int) {
        val msg = mContext.getString(stringID)
        showTipsImpl(msg,Toast.LENGTH_SHORT)
    }

    fun showTips(msg: String?) {
        showTipsImpl(msg,Toast.LENGTH_SHORT)
    }

    fun showTips(@StringRes stringID:Int, duration:Int) {
        val msg = mContext.getString(stringID)
        showTipsImpl(msg,duration)
    }

    fun showTips(msg: String?, duration: Int) {
        showTipsImpl(msg,duration)
    }

    fun showSuccessTips(msg: String?) {
        showTipsImpl(msg,Toast.LENGTH_SHORT, R.drawable.widget_toast_success)
    }

    fun showSuccessTips(@StringRes stringID: Int) {
        val msg = mContext.getString(stringID)
        showTipsImpl(msg,Toast.LENGTH_SHORT,R.drawable.widget_toast_success)
    }

    fun showWarningTips(msg: String?) {
        showTipsImpl(msg,Toast.LENGTH_SHORT, R.drawable.widget_toast_warning)
    }

    fun showWarningTips(@StringRes stringID: Int) {
        val msg = mContext.getString(stringID)
        showTipsImpl(msg,Toast.LENGTH_SHORT,R.drawable.widget_toast_warning)
    }

    private fun showTipsImpl(msg: String?, duration: Int, @DrawableRes drawableID: Int = 0) {
        if (msg.isNullOrEmpty()) {
            return
        }

        toast?.let {
            cancel()
            toast = null
        }

        mToastHandler?.postDelayed({
            try {
                toast = Toast(mContext)
                toast?.view = mBinding.root
                mBinding.tipToastTxt.text = msg
                mBinding.tipToastTxt.setCompoundDrawablesWithIntrinsicBounds(drawableID, 0, 0, 0)
                toast?.setGravity(Gravity.CENTER,0,0)
                toast?.duration = duration
                toast?.show()
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtil.e("$e")
            }
        }, 50)
    }

    private fun cancel() {
        toast?.cancel()
        mToastHandler?.removeCallbacksAndMessages(null)
    }
}