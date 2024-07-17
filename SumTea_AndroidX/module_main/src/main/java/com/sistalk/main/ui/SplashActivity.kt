package com.sistalk.main.ui

import android.annotation.SuppressLint
import android.os.Bundle
import com.sistalk.framework.base.BaseDataBindActivity
import com.sistalk.framework.ext.onClick
import com.sistalk.framework.utils.StatusBarSettingHelper
import com.sistalk.main.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseDataBindActivity<ActivitySplashBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarSettingHelper.setStatusBarTranslucent(this)
        mBinding.tvSkip.onClick {

        }

    }

}