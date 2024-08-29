package com.sistalk.banner.utils

import android.content.res.Resources
import com.sistalk.banner.options.IndicatorOptions

object IndicatorUtils {
    @JvmStatic
    fun dp2Px(dpValue: Float): Int {
        return (0.5f + dpValue * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun getCoordinateX(indicatorOptions: IndicatorOptions, maxDiameter: Float, index: Int): Float {
        val normalIndicatorWidth = indicatorOptions.normalSliderWidth
        return maxDiameter / 2 + (normalIndicatorWidth + indicatorOptions.sliderGap) * index
    }

    fun getCoordinateY(maxDiameter: Float): Float {
        return maxDiameter / 2
    }

}