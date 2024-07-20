package com.sistalk.main

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sistalk.common.constant.MAIN_ACTIVITY_HOME
import com.sistalk.framework.base.BaseDataBindActivity
import com.sistalk.main.databinding.ActivityMainBinding

@Route(path = MAIN_ACTIVITY_HOME)
class MainActivity : BaseDataBindActivity<ActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

    }
}