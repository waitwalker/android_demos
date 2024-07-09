package com.sistalk.framework.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.sistalk.framework.R
import com.sistalk.framework.toast.TipsToast
import com.sistalk.framework.utils.LoadingUtils

/**
 * activity基类
 * */
abstract class BaseActivity : AppCompatActivity() {

    protected var TAG = this::class.java.simpleName

    private val dialogUtils by lazy {
        LoadingUtils(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout()
        initView(savedInstanceState)
        initData()
    }


    /**
     * 设置布局
     * */
    open fun setContentLayout() {
        setContentView(getLayoutResId())
    }

    /**
     * 初始化视图
     * */
    abstract fun getLayoutResId():Int

    /**
     * 初始化布局
     * */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     * */
    open fun initData() {}

    fun showLoading() {
        showLoading(getString(R.string.default_loading))
    }

    fun showLoading(msg:String?) {
        dialogUtils.show(msg)
    }

    fun showLoading(@StringRes res:Int) {
        showLoading(getString(res))
    }

    fun dismissLoading() {
        dialogUtils.dismiss()
    }

    fun showToast(msg:String) {
        TipsToast.showTips(msg)
    }

    fun showToast(@StringRes resId:Int) {
        TipsToast.showTips(resId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}