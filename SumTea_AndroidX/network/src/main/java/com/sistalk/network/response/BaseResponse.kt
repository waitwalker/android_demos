package com.sistalk.network.response

data class BaseResponse<out T>(
    val data: T?,
    val errorCode: Int = 0,
    val errorMsg:String = ""
) {

    /// 是否失败， 不等于0表示失败
    fun isFailed():Boolean {
        return errorCode != 0
    }
}