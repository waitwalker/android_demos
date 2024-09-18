package com.sistalk.common.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.sistalk.common.service.ISearchService

/**
 * 搜索服务类，对话提供相应的能力
 * */
object SearchServiceProvider {
    @Autowired(name = "/search/service/search22")
    lateinit var searchService:ISearchService

    init {
        ARouter.getInstance().inject(this)
    }

    fun toSearch(context: Context) {
        searchService.toSearch(context)
    }

    fun clearSearchHistoryCache() {
        searchService.clearSearchHistoryCache()
    }
}