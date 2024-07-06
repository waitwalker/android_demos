package com.sistalk.framework.log

import android.app.Application
import android.util.Log

object LogUtil {
    private const val TAG = "LogUtil"
    private var application:Application? = null

    private var isDebug = false
    private var mLogPath:String? = null

    fun init(application: Application, logPath: String, isDebug: Boolean = false) {
        LogUtil.application = application
        LogUtil.isDebug = isDebug
        mLogPath = logPath
    }

    @JvmOverloads
    fun v(message:String,throwable: Throwable? = null, tag: String? = null, saveLog: Boolean = false) {
        prepareLog(Log.VERBOSE,tag,message,throwable,saveLog)
    }

    @JvmOverloads
    fun d(message:String,throwable: Throwable? = null, tag: String? = null, saveLog: Boolean = false) {
        prepareLog(Log.DEBUG,tag,message,throwable,saveLog)
    }

    @JvmOverloads
    fun i(message:String,throwable: Throwable? = null, tag: String? = null, saveLog: Boolean = false) {
        prepareLog(Log.INFO,tag,message,throwable,saveLog)
    }

    @JvmOverloads
    fun w(message:String,throwable: Throwable? = null, tag: String? = null, saveLog: Boolean = false) {
        prepareLog(Log.WARN,tag,message,throwable,saveLog)
    }

    @JvmOverloads
    fun e(message:String,throwable: Throwable? = null, tag: String? = null, saveLog: Boolean = false) {
        prepareLog(Log.ERROR,tag,message,throwable,saveLog)
    }

    private fun prepareLog(priority:Int,tag:String?,message:String,throwable: Throwable?,saveLog:Boolean=false){
        val logTag = tag?: TAG
        throwable?.let {

        }?:run {
            when(priority) {
                Log.VERBOSE->Log.v(logTag,message)
                Log.DEBUG->Log.d(logTag,message)
                Log.INFO->Log.i(logTag,message)
                Log.WARN->Log.w(logTag,message)
                Log.ERROR->Log.e(logTag,message)
                else->Log.v(logTag,message)
            }
        }
    }

}