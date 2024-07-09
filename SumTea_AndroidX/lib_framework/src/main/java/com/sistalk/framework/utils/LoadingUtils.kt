package com.sistalk.framework.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import com.sistalk.framework.R
import com.sistalk.framework.loading.CenterLoadingView

class LoadingUtils(private val mContext: Context) {
    private var loadView:CenterLoadingView? = null


    /**
     * 显示loading
     * */
    fun show(txt:String?) {
        if (loadView == null) {
            loadView = CenterLoadingView(mContext, R.style.dialog)
        }
        if (loadView?.isShowing == true) {
            loadView?.dismiss()
        }

        if (!TextUtils.isEmpty(txt)) {
            loadView?.setTitle(txt as CharSequence)
        }

        if (mContext is Activity && mContext.isFinishing) {
            return
        }
        loadView?.show()
    }

    fun dismiss() {
        if (mContext is Activity && mContext.isFinishing) {
            return
        }
        loadView?.let {
            if (it.isShowing) {
               it.dismiss()
            }
        }
    }

}