package com.sistalk.network.error

/**
 * 异常结果类
 * */
open class ApiException:Exception {
    var errCode:Int
    var errMsg:String

    constructor(error: Error,e:Throwable? = null):super(e) {
        errCode = error.co
    }


}