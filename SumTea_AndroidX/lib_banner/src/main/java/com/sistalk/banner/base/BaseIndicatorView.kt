package com.sistalk.banner.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.sistalk.banner.indicator.IIndicator
import com.sistalk.banner.options.IndicatorOptions

@Suppress("UNUSED")
open class BaseIndicatorView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : View(context, attrs, defStyleAttr), IIndicator {
    var mIndicatorOptions:IndicatorOptions = IndicatorOptions()

    private var mViewPager: ViewPager? = null
    private var mViewPager2: ViewPager2? = null

    private val mOnPageChangeCallback = object :ViewPager2.OnPageChangeCallback(){
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            this@BaseIndicatorView.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            this@BaseIndicatorView.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            this@BaseIndicatorView.onPageScrollStateChanged(state)
        }
    }

    init {
        mIndicatorOptions = IndicatorOptions()
    }


}