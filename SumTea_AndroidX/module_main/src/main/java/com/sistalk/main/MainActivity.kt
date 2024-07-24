package com.sistalk.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.sistalk.common.constant.KEY_INDEX
import com.sistalk.common.constant.MAIN_ACTIVITY_HOME
import com.sistalk.framework.base.BaseDataBindActivity
import com.sistalk.framework.log.LogUtil
import com.sistalk.framework.utils.StatusBarSettingHelper
import com.sistalk.main.databinding.ActivityMainBinding
import com.sistalk.main.navigator.SumFragmentNavigator

@Route(path = MAIN_ACTIVITY_HOME)
class MainActivity : BaseDataBindActivity<ActivityMainBinding>() {

    private lateinit var navController: NavController

    companion object {
        fun start(context: Context, index: Int = 0) {
            val indent = Intent(context, MainActivity::class.java)
            indent.putExtra(KEY_INDEX, index)
            context.startActivity(indent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        val bottomNav = mBinding.bottomNav
        // 1.寻找出路路由控制器对象，它是路由跳转的唯一入口，找到宿主NavHostFragment
        navController = findNavController(R.id.fragmentContainerView)
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        // 2.自定义FragmentNavigator,mobile_navigation.xml文件中的fragment标识改为SumFragmentNavigator的sumFragment
        val fragmentNavigator = SumFragmentNavigator(this, navHost.childFragmentManager, navHost.id)
        // 3.注册到Navigator里面
        navController.navigatorProvider.addNavigator(fragmentNavigator)
        // 4.设置Graph，需要将activity_main.xml文件中的app:navGraph移除
        navController.setGraph(R.navigation.mobile_navigation)
        // 5. 将NavController和BottomNavigationView绑定
        bottomNav.setupWithNavController(navController)
        StatusBarSettingHelper.setStatusBarTranslucent(this)
        StatusBarSettingHelper.statusBarLightMode(this@MainActivity, true)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let {
            val index = it.getIntExtra(KEY_INDEX,0)
            LogUtil.i("onNewIntent:index:$index", tag = TAG)
        }
    }
}