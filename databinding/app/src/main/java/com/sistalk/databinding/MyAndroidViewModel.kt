package com.sistalk.databinding

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sistalk.databinding.app.MyApplication

class MyViewModelFactory(private val application: MyApplication, private val handle: SavedStateHandle) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyAndroidViewModel::class.java)) {
            return MyAndroidViewModel(application,handle) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}

public class MyAndroidViewModel(private val application: MyApplication, private val handle: SavedStateHandle):AndroidViewModel(application) {

    private val key:String = getApplication<Application>().resources.getString(R.string.data_key)
    private val shpName:String = getApplication<Application>().resources.getString(R.string.shp_name)

    private fun load() {
        val shp:SharedPreferences = getApplication<Application>().getSharedPreferences(shpName,Context.MODE_PRIVATE)
        val x:Int = shp.getInt(key,0)
        handle[key] = x
    }

    public fun getNumber():LiveData<Int> {
        if (!handle.contains(key)) {
            load()
        }
        return handle.getLiveData(key)
    }

    @SuppressLint("CommitPrefEdits")
    fun save(value:Int) {
        val shp:SharedPreferences = getApplication<Application>().getSharedPreferences(shpName,Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = shp.edit()
        editor.putInt(key,value)
        editor.apply()
    }

    public fun add(x: Int) {
        handle[key] = getNumber().value?.plus(x)
    }

}