package com.sistalk.banner.annotation

import android.annotation.SuppressLint
import androidx.annotation.IntDef
import com.sistalk.banner.mode.IndicatorGravity
import java.lang.annotation.ElementType
import java.lang.annotation.Target

@SuppressLint("SupportAnnotationUsage")
@IntDef(IndicatorGravity.CENTER,IndicatorGravity.START,IndicatorGravity.END)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(ElementType.PARAMETER)
annotation class AIndicatorGravity()
