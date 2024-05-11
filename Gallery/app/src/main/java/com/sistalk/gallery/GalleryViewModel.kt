package com.sistalk.gallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import kotlin.math.ceil

class GalleryViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _photoListLive = MutableLiveData<List<PhotoItem>>()
    val photoListLive: LiveData<List<PhotoItem>> get() = _photoListLive

    private val keyWords =
        arrayOf("cat", "dog", "car", "beauty", "phone", "computer", "flowers", "animal")

    // 这个自己实现加载更多控制，系统有API已经实现好了
    private var currentPage = 1
    private var totalPage = 1
    private var currentKey = ""
    private var isNewQuery = true
    private var isLoading = false
    private val perPage = 20

    init {
        resetQuery()
    }

    fun resetQuery() {
        currentPage = 1
        totalPage = 1
        currentKey = keyWords.random()
        Log.d("GalleryViewModel","请求的key=$currentKey")
        isNewQuery = true
        fetchData()
    }

    fun fetchData() {
        if (isLoading) return
        isLoading = true
        if (currentPage > totalPage) return
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            {
                // with 操作，将
                with(Gson().fromJson(it, Pixabay::class.java)) {
                    totalPage = ceil(totalHits.toDouble()/perPage).toInt()
                    if (isNewQuery) {
                        _photoListLive.value = hits.toList()
                    } else {
                        _photoListLive.value = arrayListOf(_photoListLive.value!!,hits.toList()).flatten()
                    }
                }
                isLoading = false
                isNewQuery = false
                currentPage++
            },
            {
                isLoading = false
                Log.e("GalleryViewModel", "fetch data error=$it")
            }
        )
        VolleySingleton.instance(application).requestQueue.add(stringRequest)
    }

    private fun getUrl(): String {
        return "https://pixabay.com/api/?key=43526529-78fdad7ba06d5dbd4c64f7872&q=$currentKey&per_page=$perPage&page=$currentPage"
    }


}