package com.sistalk.banner.indicator

import androidx.viewpager.widget.ViewPager

interface IIndicator:ViewPager.OnPageChangeListener {
    fun notifyDataChanged()
    fun setIndicatorOptions(options:)
}