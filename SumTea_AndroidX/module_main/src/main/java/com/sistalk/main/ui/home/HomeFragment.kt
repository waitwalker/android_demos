package com.sistalk.main.ui.home

import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.sistalk.common.model.ProjectTabItem
import com.sistalk.framework.adapter.ViewPage2FragmentAdapter
import com.sistalk.framework.base.BaseMVVMFragment
import com.sistalk.main.R
import com.sistalk.main.databinding.FragmentHomeBinding
import com.sistalk.main.ui.home.viewmodel.HomeViewModel

class HomeFragment : BaseMVVMFragment<FragmentHomeBinding,HomeViewModel>(),OnRefreshListener {

    private val mArrayTabFragments = SparseArray<Fragment>()

    private var mTabLayoutMediator:TabLayoutMediator? = null
    private var mFragmentAdapter: ViewPage2FragmentAdapter? = null
    private var mProjectTabs:MutableList<ProjectTabItem> = mutableListOf()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBinding?.refreshLayout?.apply {
            autoRefresh()
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener(this@HomeFragment)
        }

        mBinding?.iv
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        TODO("Not yet implemented")
    }

}