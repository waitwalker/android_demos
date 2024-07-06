package com.sistalk.framework.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.sistalk.framework.log.LogUtil
import java.io.FileInputStream
import java.net.NetworkInterface

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

    fun init(context: Context) {
        appContext = context.applicationContext
        initDeviceInfo()
    }

    private fun initDeviceInfo() {
        initImei()
        initMac()
        initAndroidID()
        phoneModel = Build.MODEL
        phoneBrand = Build.BRAND
        phoneManufacturer = Build.MANUFACTURER
        phoneDevice = Build.DEVICE
    }

    @SuppressLint("HardwareIds")
    private fun initAndroidID() {
        if (androidID.isNotEmpty()) return
        LogUtil.d("start init device android id")
        val tmpAndroidID = Settings.Secure.getString(appContext.contentResolver,Settings.Secure.ANDROID_ID,)
        if (tmpAndroidID.isNullOrEmpty()) {
            androidID = tmpAndroidID.lowercase()
        }
    }

    private fun initMac() {
        if (mac.isNotEmpty() || wifiMacAddress.isNotEmpty() || wifiSSID.isNotEmpty()) return

        try {
            LogUtil.d("start init device mac, wifi mac and ssid")
            val vm = appContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            initMacAddress()
            if (!vm.connectionInfo.bssid.isNullOrEmpty()) {
                wifiMacAddress = vm.connectionInfo.bssid
            }

            if (!vm.connectionInfo.ssid.isNullOrEmpty()) {
                wifiSSID = vm.connectionInfo.ssid
            }
        } catch (e:Exception) {
            LogUtil.e("init mac error = ${e.stackTrace}", throwable = e)
        }
    }

    private fun initMacAddress() {
        try {
            // 6.0~7.0读取设备文件获取
            val arrStrings = arrayOf("/sys/class/net/wlan0/address","/sys/devices/virtual/net/wlan0/address")
            for (str in arrStrings) {
                mac = readFile(str)
                break
            }
            if (mac.isNotEmpty()) {
                return
            }

            // 7.0以上获取
            val interfaces = NetworkInterface.getNetworkInterfaces()?:return
            while (interfaces.hasMoreElements()) {
                val netInfo = interfaces.nextElement()
                if ("wlan0" == netInfo.name) {
                    val addresses = netInfo.hardwareAddress
                    if (addresses == null || addresses.isEmpty()) {
                        continue
                    }
                    mac = macByte2String(addresses)
                    break
                }
            }
        } catch (e:Exception) {
            LogUtil.e("read mac error = ${e.stackTrace}", throwable = e)
        }
    }

    @SuppressLint("HardwareIds")
    private fun initImei() {
        if (imei.isNotEmpty()|| imsi.isNotEmpty()) return
        if (appContext.packageManager.checkPermission(android.Manifest.permission.READ_PHONE_STATE,
                appContext.packageName) != PackageManager.PERMISSION_GRANTED) return
        try {
            LogUtil.d("start init device imei and imsi")
            val tm = appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val tmpImei: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tm.imei
            } else {
                tm.deviceId
            }
            if (tmpImei.isNotEmpty()) {
                imei = tmpImei.lowercase()
            }

            val  tmpImsi = tm.subscriberId
            if (!tmpImsi.isNullOrEmpty()) {
                imsi = tmpImsi
            }
        } catch (e:Exception) {
            LogUtil.e("init imei and imsi error = ${e.stackTrace}", throwable = e)
        }
    }

    private fun macByte2String(bytes:ByteArray):String {
        val buf = StringBuffer()
        for (b in bytes) {
            buf.append(String.format("%02X:",b))
        }
        if (buf.isNotEmpty()) {
            buf.deleteCharAt(buf.length - 1)
        }
        return buf.toString()
    }

    private fun readFile(filePath:String):String {
        var res = ""
        var fin:FileInputStream? = null
        try {
            fin = FileInputStream(filePath)
            val length = fin.available()
            val buffer = ByteArray(length)
            val count = fin.read(buffer)
            if (count > 0) {
                res = String(buffer,Charsets.UTF_8)
            }
        } catch (e:Exception) {
            LogUtil.e("read file error = ${e.stackTrace}", throwable = e)
        } finally {
            try {
                fin?.close()
            } catch (e:Exception) {
                LogUtil.e("close FileInputStream error = ${e.stackTrace}", throwable = e)
            }
        }
        return res
    }

}