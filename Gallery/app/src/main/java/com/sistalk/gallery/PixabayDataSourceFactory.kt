package com.sistalk.gallery

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

// 数据源工厂类，用于实例化数据源
class PixabayDataSourceFactory(private val context: Context):DataSource.Factory<Int,PhotoItem>() {
    private val _pixabayDataSource = MutableLiveData<PixabayDataSource>()
    var pixabayDataSource:LiveData<PixabayDataSource> = _pixabayDataSource

    override fun create(): DataSource<Int, PhotoItem> {
        return PixabayDataSource(context).also {
            _pixabayDataSource.postValue(it)
        }
    }
}