package com.sistalk.banner.annotation

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.IntDef
import java.lang.annotation.ElementType
import java.lang.annotation.Target

@SuppressLint("SupportAnnotationUsage")
@IntDef(View.VISIBLE,View.INVISIBLE,View.GONE)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(ElementType.PARAMETER)
annotation class Visibility()
