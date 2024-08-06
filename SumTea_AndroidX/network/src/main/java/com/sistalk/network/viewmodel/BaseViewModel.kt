package com.sistalk.network.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sistalk.framework.log.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel :ViewModel(){

    /**
     * 主线程中
     * */
    fun launchUI(errorBlock:(Int?,String?) -> Unit, responseBlock:suspend ()->Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            safeApiCall
        }
    }

    suspend fun <T>safeApiCall(errorBlock: suspend (Int?, String?) -> Unit, responseBlock: suspend () -> T?) :T? {
        try {
            return responseBlock()
        } catch (e:Exception) {
            e.printStackTrace()
            LogUtil.e("$e")
            val exception = Exception
        }
    }
}