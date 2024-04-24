package com.sistalk.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest

class GalleryViewModel(private val application: Application):AndroidViewModel(application) {
    private var _photoListLive = MutableLiveData<List<PhotoItem>>()
    val photoListLive:LiveData<List<PhotoItem>> get() = _photoListLive

    fun fetchData() {
        val stringRequest = StringRequest(
            Request.Method.GET,
        )
    }

}