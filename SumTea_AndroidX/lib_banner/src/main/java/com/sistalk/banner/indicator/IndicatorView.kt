package com.sistalk.banner.indicator

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.sistalk.banner.base.BaseIndicatorView
import com.sistalk.banner.controller.AttrsController
import com.sistalk.banner.drawer.DrawerProxy
import com.sistalk.banner.mode.IndicatorOrientation
import com.sistalk.banner.options.IndicatorOptions

class IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseIndicatorView(context, attrs, defStyleAttr) {

    private var mDrawerProxy: DrawerProxy

    init {
        AttrsController.initAttrs(context, attrs, mIndicatorOptions)
        mDrawerProxy = DrawerProxy(mIndicatorOptions)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mDrawerProxy.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measureResult = mDrawerProxy.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureResult.measureWidth, measureResult.measureHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rotateCanvas(canvas)
        mDrawerProxy.onDraw(canvas)
    }

    override fun setIndicatorOptions(options: IndicatorOptions) {
        super.setIndicatorOptions(options)
        mDrawerProxy.setIndicatorOptions(options)
    }

    override fun notifyDataChanged() {
        mDrawerProxy = DrawerProxy(mIndicatorOptions)
        super.notifyDataChanged()
    }

    private fun rotateCanvas(canvas: Canvas) {
        if (mIndicatorOptions.orientation == IndicatorOrientation.INDICATOR_VERTICAL) {
            canvas.rotate(90f, width / 2f, width / 2f)
        } else {
            canvas.rotate(180f, width / 2f, height / 2f)
        }
    }
}