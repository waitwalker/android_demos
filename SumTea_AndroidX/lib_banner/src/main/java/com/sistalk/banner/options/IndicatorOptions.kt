package com.sistalk.banner.options

import com.sistalk.banner.annotation.AIndicatorOrientation
import com.sistalk.banner.annotation.AIndicatorSlideMode
import com.sistalk.banner.annotation.AIndicatorStyle
import com.sistalk.banner.mode.IndicatorOrientation

// 指示器
class IndicatorOptions {
    @AIndicatorOrientation
    var orientation = IndicatorOrientation.INDICATOR_HORIZONTAL

    @AIndicatorStyle
    var indicatorStyle:Int = 0

    @AIndicatorSlideMode
    var slideMode:Int = 0

}