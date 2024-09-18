package com.sistalk.common.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface ISearchService:IProvider {

    // 跳转搜索页
    fun toSearch(context: Context)

    // 清除搜索记录
    fun clearSearchHistoryCache()
}