package com.sistalk.banner.drawer

import android.graphics.Canvas
import com.sistalk.banner.options.IndicatorOptions

class DashDrawer internal constructor(indicatorOptions: IndicatorOptions) :
    RectDrawer(indicatorOptions) {
    override fun drawDash(canvas: Canvas) {
        canvas.drawRect(mRectF, mPaint)
    }
}