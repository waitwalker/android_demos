package com.sistalk.main.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.sistalk.common.constant.MAIN_SERVICE_HOME
import com.sistalk.common.service.IMainService
import com.sistalk.main.MainActivity

@Route(path = MAIN_SERVICE_HOME)
class MainService:IMainService {
    override fun toMain(context: Context, index: Int) {
        MainActivity.start(context, index)
    }

    override fun toArticleDetail(context: Context, url: String, title: String) {

    }

    override fun init(context: Context?) {

    }
}