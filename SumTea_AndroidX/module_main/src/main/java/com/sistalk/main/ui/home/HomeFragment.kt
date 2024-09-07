package com.sistalk.main.ui.home

import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.sistalk.framework.base.BaseMVVMFragment
import com.sistalk.main.R
import com.sistalk.main.databinding.FragmentHomeBinding
import com.sistalk.main.ui.home.viewmodel.HomeViewModel

class HomeFragment : BaseMVVMFragment<FragmentHomeBinding,HomeViewModel>(),OnRefreshListener {

    private val mArrayTabFragments = SparseArray<Fragment>()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        TODO("Not yet implemented")
    }

}