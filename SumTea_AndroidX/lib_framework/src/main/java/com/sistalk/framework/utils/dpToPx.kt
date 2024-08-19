package com.sistalk.framework.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.Dimension
import com.sistalk.framework.helper.SumAppHelper


fun dpToPx(dpValue:Float):Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,SumAppHelper.getApplication().resources.displayMetrics)
}

fun dpToPx(dpValue: Int):Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue.toFloat(),SumAppHelper.getApplication().resources.displayMetrics).toInt()
}

fun dpToPx(context: Context, @Dimension(unit = Dimension.DP) dp:Int):Float {
    val r = context.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp.toFloat(),r.displayMetrics)
}

fun spToPx(spValue: Float):Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,spValue,SumAppHelper.getApplication().resources.displayMetrics)
}