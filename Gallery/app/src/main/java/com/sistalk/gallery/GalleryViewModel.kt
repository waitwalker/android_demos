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

class GalleryViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _photoListLive = MutableLiveData<List<PhotoItem>>()
    val photoListLive: LiveData<List<PhotoItem>> get() = _photoListLive

    fun fetchData() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            {
                _photoListLive.value = Gson().fromJson(it, Pixabay::class.java).hits.toList()
            },
            {
                Log.e("GalleryViewModel", "fetch data error=$it")
            }
        )
        VolleySingleton.instance(application).requestQueue.add(stringRequest)
    }

    private fun getUrl(): String {
        return "https://pixabay.com/api/?key=43526529-78fdad7ba06d5dbd4c64f7872&q=${keyWords.random()}&per_page=100"
    }

    private val keyWords =
        arrayOf("cat", "dog", "car", "beauty", "phone", "computer", "flowers", "animal")

}