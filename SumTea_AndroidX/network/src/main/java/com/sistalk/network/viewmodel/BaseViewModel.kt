package com.sistalk.network.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sistalk.framework.log.LogUtil
import com.sistalk.network.callback.IApiErrorCallback
import com.sistalk.network.error.ApiException
import com.sistalk.network.error.ERROR
import com.sistalk.network.error.ExceptionHandler
import com.sistalk.network.flow.requestFlow
import com.sistalk.network.response.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

open class BaseViewModel : ViewModel() {

    /**
     * 主线程中
     * */
    fun launchUI(errorBlock: (Int?, String?) -> Unit, responseBlock: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            safeApiCall(errorBlock = errorBlock, responseBlock)
        }
    }

    suspend fun <T> safeApiCall(
        errorBlock: suspend (Int?, String?) -> Unit,
        responseBlock: suspend () -> T?
    ): T? {
        try {
            return responseBlock()
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtil.e("$e")
            val exception = ExceptionHandler.handleException(e)
            errorBlock(exception.errCode, exception.errMsg)
        }
        return null
    }

    fun <T> launchUIWithResult(
        responseBlock: suspend () -> BaseResponse<T>?,
        errorCall: IApiErrorCallback?,
        successBlock: (T?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = safeApiCallWithResult(errorCall = errorCall, responseBlock)
            successBlock(result)
        }
    }

    suspend fun <T> safeApiCallWithResult(
        errorCall: IApiErrorCallback?,
        responseBlock: suspend () -> BaseResponse<T>?,
    ): T? {
        try {
            val response = withContext(Dispatchers.IO) {
                withTimeout(10 * 1000) {
                    responseBlock()
                }
            } ?: return null
            if (response.isFailed()) {
                throw ApiException(response.errorCode, response.errorMsg)
            }
            return response.data
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtil.e("safeApiCallWithResult error = $e")
            val exception = ExceptionHandler.handleException(e)
            if (ERROR.NOT_LOGIN.code == exception.errCode) {
                errorCall?.onLoginFail(exception.errCode, exception.errMsg)
            } else {
                errorCall?.onError(exception.errCode, exception.errMsg)
            }
        }
        return null
    }

    fun <T> launchFlow(
        errorCall: IApiErrorCallback? = null,
        responseCall: suspend () -> BaseResponse<T>?,
        showLoading:((Boolean)->Unit)?= null,
        successBlock:(T?)->Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val data = requestFlow(errorBlock = {code, error->
                if (ERROR.NOT_LOGIN.code == code) {
                    errorCall?.onLoginFail(code,error)
                } else {
                    errorCall?.onError(code,error)
                }
            },responseCall,showLoading)
            successBlock(data)
        }
    }
}