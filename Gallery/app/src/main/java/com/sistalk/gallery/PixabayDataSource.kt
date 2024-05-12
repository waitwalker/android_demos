package com.sistalk.gallery

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

// 之前java中枚举是一个对象类型，占据更多的空间，建议写成常量，尽量少用枚举类
// kotlin，enum class和Int都是对象类型，建议使用枚举类

enum class NetworkStatus {
    LOADING,
    FAILED,
    COMPLETED
}
class PixabayDataSource(private val context: Context) : PageKeyedDataSource<Int, PhotoItem>() {

    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus:LiveData<NetworkStatus> get() = _networkStatus

    private val queryKey =
        arrayOf("cat", "dog", "car", "beauty", "phone", "computer", "flowers", "animal").random()

    // 第一个
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PhotoItem>
    ) {
        _networkStatus.postValue(NetworkStatus.LOADING)
        val url =
            "https://pixabay.com/api/?key=43526529-78fdad7ba06d5dbd4c64f7872&q=$queryKey&per_page=$50&page=1"
        StringRequest(
            Request.Method.GET, url,
            {
                val dataList: List<PhotoItem> =
                    Gson().fromJson(it, Pixabay::class.java).hits.toList()
                callback.onResult(dataList, null, nextPageKey = 2)
            },
            {
                Log.e("dataSource", "loadInitial:$it")
                _networkStatus.postValue(NetworkStatus.FAILED)
            },
        ).also {
            VolleySingleton.instance(context).requestQueue.add(it)
        }
    }


    // 下一页
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {
        val url =
            "https://pixabay.com/api/?key=43526529-78fdad7ba06d5dbd4c64f7872&q=$queryKey&per_page=$50&page=${params.key}"
        StringRequest(
            Request.Method.GET, url,
            {
                val dataList: List<PhotoItem> =
                    Gson().fromJson(it, Pixabay::class.java).hits.toList()
                callback.onResult(dataList, params.key+1)
            },
            {
                Log.e("dataSource", "loadInitial:$it")
                _networkStatus.postValue(NetworkStatus.FAILED)
            },
        ).also {
            VolleySingleton.instance(context).requestQueue.add(it)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {
        TODO("Not yet implemented")
    }


}