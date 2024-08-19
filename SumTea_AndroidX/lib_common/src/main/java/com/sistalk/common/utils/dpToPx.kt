package com.sistalk.common.utils

import android.util.TypedValue
import com.sistalk.framework.helper.*

/// dp 转 像素
fun dpToPx(dpValue:Float):Float {

    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,SumAppHelper.getApplication())
}