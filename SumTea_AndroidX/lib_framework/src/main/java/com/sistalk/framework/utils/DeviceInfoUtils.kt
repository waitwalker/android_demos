package com.sistalk.framework.utils

import android.content.Context

/**
 * 设备信息的单例工具类
 * */
object DeviceInfoUtils {
    private lateinit var appContext:Context

    // 设备imei号
    var imei:String = ""

    // 设备imsi号
    var imsi:String = ""

    // androidID
    var androidID:String = ""

    var mac:String = ""
        private set

    var wifiMacAddress:String = ""
        private set

    var wifiSSID:String = ""
        private set

    var phoneModel:String = ""
        private set

    var phoneBrand:String = ""
        private set

    var phoneManufacturer:String = ""
        private set
    var phoneDevice:String = ""
        private set

}