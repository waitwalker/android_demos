package com.sistalk.framework.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/// ViewPage2 Adapter
/// 用于绑定数据
class ViewPage2FragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var fragments: SparseArray<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(i: Int): Fragment {
        return fragments[i]
    }

    override fun getItemCount(): Int {
        return fragments.size()
    }

    fun setData(fragments: SparseArray<Fragment>) {
        this.fragments = fragments
    }

}