package com.sistalk.starter.utils

import android.app.ActivityManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Process
import android.text.TextUtils
import android.util.Log
import com.sistalk.framework.log.LogUtil
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

object StarterUtils {
    val tag = "StarterUtils"
    private var sCurProcessName: String? = null


    fun isMainProcess(context: Context):Boolean {
        val processName = getCurProcessName(context)
        return if (processName != null && processName.contains(":")) {
            false
        } else processName != null && processName == context.packageName
    }

    fun isNetworkConnected(context: Context?):Boolean {
        if (context != null) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return networkCapabilities != null
        }
        return false
    }


    private fun getCurProcessName(context: Context):String? {
        val proName = sCurProcessName
        if (!TextUtils.isEmpty(proName)) {
            return proName
        }

        try {
            val pid = Process.myPid()
            val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (appProcess in mActivityManager.runningAppProcesses) {
                if (appProcess.pid == pid) {
                    sCurProcessName = appProcess.processName
                    return sCurProcessName
                }
            }
        } catch (e:Exception) {
            LogUtil.e("${e.stackTrace}", tag = "StarterUtil")
        }
        sCurProcessName = curProcessNameFromPro
        return sCurProcessName
    }


    private val curProcessNameFromPro:String? get() {
        var cmdlineReader:BufferedReader? = null
        try {
            cmdlineReader = BufferedReader(
                InputStreamReader(FileInputStream("proc/${Process.myPid()}/cmdline"),"iso-8859-1")
            )
            var c:Int
            val processName = StringBuilder()
            while (cmdlineReader.read().also { c = it } > 0) {
                processName.append(c.toChar())
            }
            return processName.toString()
        } catch (e:Exception) {
            LogUtil.e("BufferedReader error = ${e.stackTrace}", tag = tag)
        } finally {
            if (cmdlineReader != null) {
                try {
                    cmdlineReader.close()
                } catch (e:Exception) {
                    LogUtil.e("cmdlineReader close error = ${e.stackTrace}", tag = tag)
                }
            }
        }
        return null
    }
}