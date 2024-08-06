package com.sistalk.network.error

enum class ERROR(val code:Int,val errMsg:String) {

    UNAUTHORIZED(401,"当前请求需要用户验证")

}