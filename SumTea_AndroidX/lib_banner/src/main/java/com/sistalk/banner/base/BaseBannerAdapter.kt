package com.sistalk.banner.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseBannerAdapter<T,H:BaseViewHolder<T>>:RecyclerView.Adapter<H>() {
    companion object {
        const val MAX_VALUE = Int.MAX_VALUE / 2
    }

    protected var mList:MutableList<T> = ArrayList()
    private var isCanLoop = false
    private var mPageClickListener:BannerViewPager
}























