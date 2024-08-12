package com.sistalk.banner
import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.lifecycle.LifecycleObserver
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.sistalk.banner.base.BaseViewHolder

open class BannerViewPager<T,H:BaseViewHolder<T>> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null):RelativeLayout(context,attrs),LifecycleObserver {
    private var currentPosition = 0
    private var isCustomIndicator = false
    private var isLooping = false
    private var mOnPageClickListener:OnPageChangeListener? = null
    private var mIndicatorView:
}