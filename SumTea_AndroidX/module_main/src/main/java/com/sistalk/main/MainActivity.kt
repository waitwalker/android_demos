package com.sistalk.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.sistalk.common.constant.KEY_INDEX
import com.sistalk.common.constant.MAIN_ACTIVITY_HOME
import com.sistalk.framework.base.BaseDataBindActivity
import com.sistalk.main.databinding.ActivityMainBinding

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
        navController = findNavController(R.id.fragmentContainerView)
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

    }
}