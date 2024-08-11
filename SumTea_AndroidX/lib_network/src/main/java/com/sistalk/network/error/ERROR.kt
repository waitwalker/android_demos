package com.sistalk.network.error

enum class ERROR(val code: Int, val errMsg: String) {

    UNAUTHORIZED(401, "当前请求需要用户验证"),

    FORBIDDEN(403, "资源不可用"),

    NOT_FOUND(404, "无法找到指定位置的资源"),

    REQUEST_TIMEOUT(408, "请求超时"),

    INTERNAL_SERVER_ERROR(500, "服务器错误"),

    BAD_GATEWAY(502, "非法应答"),

    SERVICE_UNAVAILABLE(503, "服务器未能应答"),

    GATEWAY_TIMEOUT(504, "服务器未能应答"),

    UNKNOWN(-1000, "未知错误"),

    PARSE_ERROR(-1001, "解析错误"),

    NETWORK_ERROR(-1002, "网络异常，请尝试刷新"),

    HTTP_ERROR(-1003, "网络协议错误"),

    SSL_ERROR(-1004, "证书出错"),

    TIMEOUT_ERROR(-1006, "连接超时"),

    NOT_LOGIN(-1007, "未登录"),

    UNKNOWN_HOST(-1008, "未知Host")

}