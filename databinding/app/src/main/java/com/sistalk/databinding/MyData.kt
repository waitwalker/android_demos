package com.sistalk.databinding

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class MyData(private val context: Context) {

    var number:Int = 0

    @SuppressLint("CommitPrefEdits")
    fun save(value:Int) {
        val name:String = context.resources.getString(R.string.my_data)
        val shp = context.getSharedPreferences(name,Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = shp.edit()
        val key:String = context.resources.getString(R.string.number)
        editor.putInt(key,value)
        editor.apply()
    }

    fun load() {
        val name:String = context.resources.getString(R.string.my_data)
        val shp:SharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE)
        val key:String = context.resources.getString(R.string.number)
        val value:Int = shp.getInt(key,0)
        println("获取到的值=$value")
    }
}