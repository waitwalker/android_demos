package com.sistalk.banner.annotation

import androidx.annotation.IntDef
import com.sistalk.banner.mode.IndicatorStyle

// 指示器样式
@IntDef(IndicatorStyle.CIRCLE, IndicatorStyle.DASH,IndicatorStyle.ROUND_RECT)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER,AnnotationTarget.FIELD)
annotation class AIndicatorStyle