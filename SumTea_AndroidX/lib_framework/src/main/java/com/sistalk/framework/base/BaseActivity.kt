package com.sistalk.framework.base

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sistalk.framework.R

/**
 * activity基类
 * */
abstract class BaseActivity : AppCompatActivity() {

    protected var TAG = this::class.java.simpleName

    private val dialogUtils by lazy {
        LoadingUtils()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_base)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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

    }

    fun showLoading(msg:String?) {

    }
}