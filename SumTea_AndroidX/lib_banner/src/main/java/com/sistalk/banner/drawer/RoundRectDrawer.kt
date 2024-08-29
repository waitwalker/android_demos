package com.sistalk.banner.drawer

import android.graphics.Canvas
import com.sistalk.banner.options.IndicatorOptions

class RoundRectDrawer internal constructor(indicatorOptions: IndicatorOptions) :
    RectDrawer(indicatorOptions) {
    override fun drawRoundRect(canvas: Canvas, rx: Float, ry: Float) {
        canvas.drawRoundRect(mRectF, rx, ry, mPaint)
    }
}