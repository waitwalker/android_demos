package com.sistalk.network.error

import java.io.IOException

/**
 * 异常结果类
 * */
open class ApiException : Exception {
    var errCode: Int
    var errMsg: String

    constructor(error: ERROR, e: Throwable? = null) : super(e) {
        errCode = error.code
        errMsg = error.errMsg
    }


    constructor(code: Int, msg: String, e: Throwable? = null) : super(e) {
        this.errCode = code
        this.errMsg = msg
    }
}

class NoNetworkException : IOException {
    var errCode: Int
    var errMsg: String

    constructor(error: ERROR, e: Throwable? = null) : super(e) {
        errCode = error.code
        errMsg = error.errMsg
    }
}