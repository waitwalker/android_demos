package com.sistalk.banner.base

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sistalk.banner.BannerViewPager
import com.sistalk.banner.utils.BannerUtils

abstract class BaseBannerAdapter<T,H:BaseViewHolder<T>>:RecyclerView.Adapter<H>() {
    companion object {
        const val MAX_VALUE = Int.MAX_VALUE / 2
    }

    protected var mList:MutableList<T> = ArrayList()
    private var isCanLoop = false

    private var mPageClickListener: BannerViewPager.OnPageClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
        val viewHolder = onCreateHolder(parent, viewType)
        viewHolder.itemView.setOnClickListener { clickedView: View? ->
            val adapterPosition = viewHolder.adapterPosition
            if (mPageClickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                val realPosition:Int = BannerUtils.getRealPosition(viewHolder.adapterPosition,getListSize())
                mPageClickListener?.onPageClick(clickedView,realPosition)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: H, position: Int) {
        val realPosition:Int = BannerUtils.getRealPosition(position,getListSize())
        onBindView(holder,mList[realPosition],realPosition,getListSize())
    }

    override fun getItemViewType(position: Int): Int {
        val realPosition:Int = BannerUtils.getRealPosition(position,getListSize())
        return getViewType(position)
    }

    override fun getItemCount(): Int {
        return if (isCanLoop && getListSize() > 1) {
            MAX_VALUE
        } else {
            getListSize()
        }
    }

    fun getData():MutableList<T> {
        return mList
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<T>?) {
        if (list != null) {
            mList.clear()
            mList.addAll(list)
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<T>?) {
        list?.let {
            mList.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun setCanLoop(canLoop:Boolean) {
        isCanLoop = canLoop
    }

    fun setPageClickListener(pageClickListener: BannerViewPager.OnPageClickListener?) {
        mPageClickListener = pageClickListener
    }

    fun getListSize():Int {
        return mList.size
    }

    protected fun getViewType(position: Int):Int {
        return 0
    }

    fun isCanLoop():Boolean {
        return isCanLoop
    }

    abstract fun onCreateHolder(parent: ViewGroup,viewType: Int):H

    abstract fun onBindView(holder: H,data:T,position: Int,pageSize:Int)

}























