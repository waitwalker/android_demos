package com.sistalk.banner.controller

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import com.sistalk.banner.R
import com.sistalk.banner.options.BannerOptions
import com.sistalk.banner.options.BannerOptions.Companion.DEFAULT_REVEAL_WIDTH
import com.sistalk.framework.utils.dpToPx

/**
 * Attribute控制器
 * */
class AttributeController(var mBannerOptions: BannerOptions) {
    fun init(context: Context?, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.BannerViewPager)
            typedArray?.let {
                initBannerAttrs(typedArray)
                initIndicatorAttrs(typedArray)
                it.recycle()
            }
        }
    }

    private fun initIndicatorAttrs(typeArray: TypedArray) {
        val indicatorCheckedColor = typeArray.getColor(
            R.styleable.BannerViewPager_bvp_indicator_checked_color,
            Color.parseColor("$8C18171C")
        )

        val indicatorNormalColor = typeArray.getColor(
            R.styleable.BannerViewPager_bvp_indicator_normal_color,
            Color.parseColor("#8C6C6D72")
        )

        val normalIndicatorWidth = typeArray.getDimension(
            R.styleable.BannerViewPager_bvp_indicator_radius,
            dpToPx(8f)
        )

        val indicatorGravity =
            typeArray.getInt(R.styleable.BannerViewPager_bvp_indicator_gravity, 0)

        val indicatorStyle = typeArray.getInt(R.styleable.BannerViewPager_bvp_indicator_style, 0)
        val indicatorSlideMode =
            typeArray.getInt(R.styleable.BannerViewPager_bvp_indicator_slide_mode, 0)
        val indicatorVisibility =
            typeArray.getInt(R.styleable.BannerViewPager_bvp_indicator_visibility, 0)
        mBannerOptions.setIndicatorSliderColor(indicatorNormalColor, indicatorCheckedColor)
        mBannerOptions.setIndicatorSliderWidth(
            normalIndicatorWidth.toInt(),
            normalIndicatorWidth.toInt()
        )
        mBannerOptions.setIndicatorGravity(indicatorGravity)
        mBannerOptions.setIndicatorStyle(indicatorStyle)
        mBannerOptions.setIndicatorSlideMode(indicatorSlideMode)
        mBannerOptions.setIndicatorVisibility(indicatorVisibility)
        mBannerOptions.setIndicatorGap(normalIndicatorWidth)
        mBannerOptions.setIndicatorHeight((normalIndicatorWidth / 2f).toInt())
    }

    private fun initBannerAttrs(typeArray: TypedArray) {
        val interval = typeArray.getInteger(R.styleable.BannerViewPager_bvp_interval, 3000)
        val isAutoPlay = typeArray.getBoolean(R.styleable.BannerViewPager_bvp_auto_play, true)
        val isCanLoop = typeArray.getBoolean(R.styleable.BannerViewPager_bvp_can_loop, true)
        val pageMargin =
            typeArray.getDimension(R.styleable.BannerViewPager_bvp_page_margin, 0f).toInt()
        val roundCorner =
            typeArray.getDimension(R.styleable.BannerViewPager_bvp_round_corner, 0f).toInt()
        val revealWidth = typeArray.getDimension(
            R.styleable.BannerViewPager_bvp_reveal_width,
            DEFAULT_REVEAL_WIDTH.toFloat()
        ).toInt()
        val pageStyle = typeArray.getInt(R.styleable.BannerViewPager_bvp_page_style, 0)
        val scrollDuration = typeArray.getInt(R.styleable.BannerViewPager_bvp_scroll_duration, 0)
        mBannerOptions.setInterval(interval.toLong())
        mBannerOptions.setAutoPlay(isAutoPlay)
        mBannerOptions.setCanLoop(isCanLoop)
        mBannerOptions.setPageMargin(pageMargin)
        mBannerOptions.setRoundRectRadius(roundCorner)
        mBannerOptions.setRightRevealWidth(revealWidth)
        mBannerOptions.setLeftRevealWidth(revealWidth)
        mBannerOptions.setPageStyle(pageStyle)
        mBannerOptions.setScrollDuration(scrollDuration)
    }
}