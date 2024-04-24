package com.sistalk.gallery

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton private constructor(context: Context){
    companion object {
        private var _instance:VolleySingleton? = null
        fun instance(context: Context)=
            _instance?: synchronized(this){
                VolleySingleton(context).also {
                    _instance = it
                }
            }
    }

    val requestQueue:RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }
}