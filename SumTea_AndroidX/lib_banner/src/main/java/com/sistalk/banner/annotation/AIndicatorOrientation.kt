package com.sistalk.banner.annotation

import androidx.annotation.IntDef
import com.sistalk.banner.mode.IndicatorOrientation

@IntDef(IndicatorOrientation.INDICATOR_HORIZONTAL, IndicatorOrientation.INDICATOR_VERTICAL,IndicatorOrientation.INDICATOR_RTL)

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class AIndicatorOrientation