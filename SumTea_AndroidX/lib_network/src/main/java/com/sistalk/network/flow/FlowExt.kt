package com.sistalk.network.flow

import com.sistalk.framework.log.LogUtil
import com.sistalk.network.error.ApiException
import com.sistalk.network.error.ExceptionHandler
import com.sistalk.network.response.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withTimeout


suspend fun <T> requestFlow(
    errorBlock:((Int?,String?)->Unit)? = null,
    requestCall:suspend ()->BaseResponse<T>?,
    showLoading:((Boolean)->Unit)? = null,
):T? {
    var data:T? = null
    val flow = requestFlowResponse(errorBlock, requestCall = requestCall,showLoading)
    // 7.调用collect获取emit()回调结果
    flow.collect {
        data = it?.data
    }
    return data
}

suspend fun <T> requestFlowResponse(
    errorBlock: ((Int?, String?) -> Unit)? = null,
    requestCall: suspend () -> BaseResponse<T>?,
    showLoading: ((Boolean) -> Unit)? = null,
    ):Flow<BaseResponse<T>?> {
    // 1.执行请求
    val flow = flow {
        // 设置超时时间
        val response = withTimeout(10*1000) {
            requestCall()
        }

        if (response?.isFailed() == true) {
            throw ApiException(response.errorCode,response.errorMsg)
        }
        // 2.发送网络请求结果回调
        emit(response)
        // 3.指定运行线程
    }.flowOn(Dispatchers.IO)
        .onStart {
            // 4.请求开始
            showLoading?.invoke(true)
        }
        .catch { e->
            // 5.捕获异常
            e.printStackTrace()
            LogUtil.e("requestFlowResponse error = $e")
            val exception = ExceptionHandler.handleException(e)
            errorBlock?.invoke(exception.errCode,exception.errMsg)
        }.onCompletion {
            //6.请求完成
            showLoading?.invoke(false)
        }
    return flow
}