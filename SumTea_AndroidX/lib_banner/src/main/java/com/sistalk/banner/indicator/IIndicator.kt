package com.sistalk.banner.indicator

import androidx.viewpager.widget.ViewPager
import com.sistalk.banner.options.IndicatorOptions

interface IIndicator:ViewPager.OnPageChangeListener {
    fun notifyDataChanged()
    fun setIndicatorOptions(options:IndicatorOptions)
}