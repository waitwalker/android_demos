package com.sistalk.banner.indicator

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import com.sistalk.banner.base.BaseIndicatorView

class DrawableIndicator @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BaseIndicatorView(context!!, attrs, defStyleAttr) {
    // 选中时的Bitmap
    private var mCheckedBitmap: Bitmap? = null

    // 未选中时的Bitmap
    private var mNormalBitmap: Bitmap? = null

    private var mIndicatorPadding = 0
    private var mCheckedBitmapWidth = 0
    private var mCheckedBitmapHeight = 0
    private var mNormalBitmapWidth = 0
    private var mNormalBitmapHeight = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val maxHeight = mCheckedBitmapHeight.coerceAtLeast(mNormalBitmapHeight)
        val realWidth =
            mCheckedBitmapWidth + (mNormalBitmapWidth + mIndicatorPadding) * (getPageSize() - 1)
        setMeasuredDimension(realWidth, maxHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (getPageSize() > 1 && mCheckedBitmap != null && mNormalBitmap != null) {
            for (i in 1 until getPageSize() + 1) {
                var left: Int
                var top: Int
                var bitmap = mNormalBitmap
                val index = i - 1
                when {
                    index < getCurrentPosition() -> {
                        left = (i - 1) * (mNormalBitmapWidth + mIndicatorPadding)
                        top = measuredHeight / 2 - mNormalBitmapHeight / 2
                    }

                    index == getCurrentPosition() -> {
                        left = (i - 1) * (mNormalBitmapWidth + mIndicatorPadding)
                        top = measuredHeight / 2 - mCheckedBitmapHeight / 2
                        bitmap = mCheckedBitmap
                    }

                    else -> {
                        left =
                            (i - 1) * mIndicatorPadding * (i - 2) * mNormalBitmapWidth + mCheckedBitmapWidth
                        top = measuredHeight / 2 - mNormalBitmapHeight / 2
                    }
                }
                drawIcon(canvas, left, top, bitmap)
            }
        }
    }

    private fun drawIcon(canvas: Canvas, left: Int, top: Int, icon: Bitmap?) {
        if (icon != null) {
            canvas.drawBitmap(icon, left.toFloat(), top.toFloat(), null)
        }
    }
}