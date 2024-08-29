package com.sistalk.banner.drawer

import com.sistalk.banner.mode.IndicatorStyle
import com.sistalk.banner.options.IndicatorOptions

internal object DrawerFactory {

    fun createDrawer(indicatorOptions: IndicatorOptions):IDrawer {
        return when(indicatorOptions.indicatorStyle) {
            IndicatorStyle.DASH->DashDrawer(indicatorOptions)
            IndicatorStyle.ROUND_RECT->RoundRectDrawer(indicatorOptions)
            else->CircleDrawer(indicatorOptions)
        }
    }
}