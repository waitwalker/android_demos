package com.sistalk.common.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.sistalk.common.constant.MAIN_SERVICE_HOME
import com.sistalk.common.service.IMainService

/**
 * MainService方法承载类
 * */
object MainServiceProvider {

    @Autowired(name = MAIN_SERVICE_HOME)
    lateinit var mainService: IMainService

    init {
        ARouter.getInstance().inject(this)
    }

    /**
     * 跳转到主页指定tab页
     * */
    fun toMain(context: Context, index: Int = 0) {
        mainService.toMain(context, index)
    }

    /**
     * 跳转到文章详情页
     * */
    fun toArticleDetail(context: Context, url: String, title: String) {
        mainService.toArticleDetail(context, url, title)
    }
}