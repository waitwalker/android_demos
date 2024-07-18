package com.sistalk.common.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider


/**
 * 跳转到主页方法
 * */
interface IMainService : IProvider {

    /**
     * 跳转到指定tab主页
     * */
    fun toMain(context: Context, index: Int)

    /**
     * 跳转到详情
     * */
    fun toArticleDetail(context: Context, url: String, title: String)
}