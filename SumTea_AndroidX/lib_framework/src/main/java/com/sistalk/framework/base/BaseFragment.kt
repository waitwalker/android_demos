package com.sistalk.framework.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.sistalk.framework.R
import com.sistalk.framework.log.LogUtil
import com.sistalk.framework.toast.TipsToast
import com.sistalk.framework.utils.LoadingUtils

/**
 * Fragment基类
 * */
abstract class BaseFragment : Fragment() {
    protected var TAG: String? = this::class.java.simpleName
    protected var mIsViewCreate = false
    private val dialogUtils by lazy {
        LoadingUtils(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mIsViewCreate = true
        initView(view,savedInstanceState)
        initData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (mIsViewCreate) {
            onFragmentVisible(isVisibleToUser)
        }
    }


    override fun onResume() {
        super.onResume()
        if (userVisibleHint) {
            onFragmentVisible(true)
        }
    }

    override fun onStop() {
        super.onStop()
        if (userVisibleHint) {
            onFragmentVisible(false)
        }
    }

    open fun onFragmentVisible(isVisibleToUser: Boolean) {
        LogUtil.w("onFragmentVisible = $isVisibleToUser", tag = TAG)
    }

    /**
     * 获取content view
     * */
    open fun getContentView(inflater: LayoutInflater, container: ViewGroup?):View {
        return inflater.inflate(getLayoutResId(),null)
    }

    /**
     * 获取布局id
     * */
    abstract fun getLayoutResId(): Int

    /**
     * 初始化视图
     * */
    abstract fun initView(view: View,savedInstanceState: Bundle?)

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

    fun dismissLoading() {
        dialogUtils.dismiss()
    }

    fun showToast(msg: String) {
        TipsToast.showTips(msg)
    }

    fun showToast(@StringRes resId:Int) {
        TipsToast.showTips(resId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}