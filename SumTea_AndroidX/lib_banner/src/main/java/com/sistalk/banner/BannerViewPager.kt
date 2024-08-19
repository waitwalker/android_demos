package com.sistalk.banner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.LifecycleObserver
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.sistalk.banner.base.BaseViewHolder
import com.sistalk.banner.indicator.IIndicator

open class BannerViewPager<T, H : BaseViewHolder<T>> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RelativeLayout(context, attrs), LifecycleObserver {
    private var currentPosition = 0
    private var isCustomIndicator = false
    private var isLooping = false
    private var mOnPageClickListener: OnPageClickListener? = null
    private var mIndicatorView:IIndicator? = null
    private var mIndicatorLayout:RelativeLayout? = null
    private var mViewPager:ViewPager2? = null
    private var mBannerManager:BannerManager = BannerManager()


    interface OnPageClickListener {
        fun onPageClick(clickedView: View?, position: Int)
    }

}

