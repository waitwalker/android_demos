package com.sistalk.banner.options

import android.graphics.Color
import com.sistalk.banner.annotation.AIndicatorOrientation
import com.sistalk.banner.annotation.AIndicatorSlideMode
import com.sistalk.banner.annotation.AIndicatorStyle
import com.sistalk.banner.mode.IndicatorOrientation
import com.sistalk.banner.mode.IndicatorSlideMode
import com.sistalk.framework.utils.dpToPx


// 指示器
class IndicatorOptions {
    @AIndicatorOrientation
    var orientation = IndicatorOrientation.INDICATOR_HORIZONTAL

    @AIndicatorStyle
    var indicatorStyle: Int = 0

    @AIndicatorSlideMode
    var slideMode: Int = 0

    // 页面size
    var pageSize: Int = 0

    /// 未选中时Indicator颜色
    var normalSliderColor:Int = 0

    // 选中时Indicator颜色
    var checkedSliderColor: Int = 0

    // Indicator间距
    var sliderGap: Float = 0f

    // 高度
    var sliderHeight: Float = 0f
        get() = if (field > 0) field else normalSliderWidth / 2

    var normalSliderWidth:Float = 0f

    var checkedSliderWidth:Float = 0f

    // 指示器当前位置
    var currentPosition:Int = 0

    // 从一个点滑动到另一个点的进度
    var sliderProgress:Float = 0f

    var showIndicatorOneItem:Boolean = false

    init {
        normalSliderWidth = dpToPx(8f)
        checkedSliderWidth = normalSliderWidth
        sliderGap = normalSliderWidth
        normalSliderColor = Color.parseColor("#8C18171C")
        checkedSliderColor = Color.parseColor("#8C6C6D72")
        slideMode = IndicatorSlideMode.NORMAL
    }

    fun setCheckedColor(checkedColor:Int) {
        this.checkedSliderColor = checkedColor
    }

    fun setSliderWidth(normalIndicatorWidth:Float,checkedIndicatorWidth:Float) {
        this.normalSliderWidth = normalIndicatorWidth
        this.checkedSliderWidth = checkedIndicatorWidth
    }

    fun setSliderWidth(sliderWidth:Float) {
        setSliderWidth(sliderWidth,sliderWidth)
    }

    fun setSliderColor(normalColor:Int, checkedColor:Int) {
        this.normalSliderColor = normalColor
        this.checkedSliderColor = checkedColor
    }


}