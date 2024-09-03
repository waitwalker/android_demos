package com.sistalk.banner.annotation

import androidx.annotation.IntDef
import com.sistalk.banner.mode.IndicatorSlideMode.Companion.NORMAL
import com.sistalk.banner.mode.PageStyle.MULTI_PAGE
import com.sistalk.banner.mode.PageStyle.MULTI_PAGE_OVERLAP
import com.sistalk.banner.mode.PageStyle.MULTI_PAGE_SCALE

@Target(AnnotationTarget.VALUE_PARAMETER)
@IntDef(NORMAL,MULTI_PAGE,MULTI_PAGE_OVERLAP,MULTI_PAGE_SCALE)
@Retention(AnnotationRetention.SOURCE)
annotation class APageStyle()
