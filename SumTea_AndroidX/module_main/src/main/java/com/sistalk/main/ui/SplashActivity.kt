package com.sistalk.main.ui
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.sistalk.common.provider.MainServiceProvider
import com.sistalk.framework.base.BaseDataBindActivity
import com.sistalk.framework.ext.onClick
import com.sistalk.framework.utils.StatusBarSettingHelper
import com.sistalk.framework.utils.countDownCoroutines
import com.sistalk.main.R
import com.sistalk.main.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseDataBindActivity<ActivitySplashBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarSettingHelper.setStatusBarTranslucent(this)
        mBinding.tvSkip.onClick {
            MainServiceProvider.toMain(this)
        }

        // 倒计时
        countDownCoroutines(2,lifecycleScope, onTick = {
            mBinding.tvSkip.text = getString(R.string.splash_time, it.plus(1).toString())
        }) {
            MainServiceProvider.toMain(this)
            finish()
        }
    }
}