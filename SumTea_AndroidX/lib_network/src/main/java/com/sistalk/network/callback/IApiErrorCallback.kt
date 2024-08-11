package com.sistalk.network.callback

import com.sistalk.framework.toast.TipsToast

/**
 * 异常回调
 * */
interface IApiErrorCallback {

    /**
     * 错误处理
     * */
    fun onError(code:Int?, error:String?) {
        TipsToast.showTips(error)
    }

    /**
     * 登录失败处理
     * */
    fun onLoginFail(code: Int?,error: String?) {
        TipsToast.showTips(error)
    }
}