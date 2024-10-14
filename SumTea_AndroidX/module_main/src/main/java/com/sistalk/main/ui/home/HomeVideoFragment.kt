package com.sistalk.main.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sistalk.framework.base.BaseMVVMFragment
import com.sistalk.main.R
import com.sistalk.main.databinding.FragmentHomeVideoBinding
import com.sistalk.main.ui.home.adapter.HomeVideoItemAdapter
import com.sistalk.main.ui.home.viewmodel.HomeViewModel

class HomeVideoFragment : BaseMVVMFragment<FragmentHomeVideoBinding,HomeViewModel>() {

    private lateinit var videoAdapter: HomeVideoItemAdapter

    override fun initView(view: View, savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

}