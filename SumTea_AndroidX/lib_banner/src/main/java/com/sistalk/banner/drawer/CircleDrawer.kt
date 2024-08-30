package com.sistalk.banner.drawer

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.RectF
import com.sistalk.banner.base.BaseDrawer
import com.sistalk.banner.mode.IndicatorSlideMode
import com.sistalk.banner.options.IndicatorOptions
import com.sistalk.banner.utils.IndicatorUtils

class CircleDrawer internal constructor(indicatorOptions: IndicatorOptions) :
    BaseDrawer(indicatorOptions) {

    private val rectF = RectF()
    override fun measureHeight(): Int {
        return maxWidth.toInt() + INDICATOR_PADDING_ADDITION
    }

    override fun onDraw(canvas: Canvas) {
        val pageSize = mIndicatorOptions.pageSize
        if (pageSize > 1 || mIndicatorOptions.showIndicatorOneItem && pageSize == 1) {
            drawNormal(canvas)
            drawSlider(canvas)
        }
    }

    private fun drawNormal(canvas: Canvas) {
        val normalIndicatorWidth = mIndicatorOptions.normalSliderWidth
        mPaint.color = mIndicatorOptions.normalSliderColor
        for (i in 0 until mIndicatorOptions.pageSize) {
            val coordinateX = IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, i)
            val coordinateY = IndicatorUtils.getCoordinateY(maxWidth)
            drawCircle(canvas, coordinateX, coordinateY, normalIndicatorWidth / 2)
        }
    }

    private fun drawSlider(canvas: Canvas) {
        mPaint.color = mIndicatorOptions.checkedSliderColor
        when (mIndicatorOptions.slideMode) {
            IndicatorSlideMode.NORMAL, IndicatorSlideMode.SMOOTH -> drawCircleSlider(canvas)
            IndicatorSlideMode.WORM -> drawWormSlider(canvas)
            IndicatorSlideMode.SCALE -> drawScaleSlider(canvas)
            IndicatorSlideMode.COLOR -> drawColor(canvas)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun drawColor(canvas: Canvas) {
        val currentPosition = mIndicatorOptions.currentPosition
        val slideProgress = mIndicatorOptions.sliderProgress
        val coordinateX =
            IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, currentPosition)
        val coordinateY = IndicatorUtils.getCoordinateY(maxWidth)
        var evaluate = argbEvaluator?.evaluate(
            slideProgress,
            mIndicatorOptions.checkedSliderColor,
            mIndicatorOptions.normalSliderColor
        )
        mPaint.color = evaluate as Int
        drawCircle(canvas, coordinateX, coordinateY, mIndicatorOptions.normalSliderWidth / 2)
        evaluate = argbEvaluator?.evaluate(
            1 - slideProgress,
            mIndicatorOptions.checkedSliderColor,
            mIndicatorOptions.normalSliderColor
        )
        mPaint.color = evaluate as Int
        val nextCoordinateX = if (currentPosition == mIndicatorOptions.pageSize - 1) {
            IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, 0)
        } else {
            coordinateX + mIndicatorOptions.sliderGap + mIndicatorOptions.normalSliderWidth
        }
        drawCircle(canvas, nextCoordinateX, coordinateY, mIndicatorOptions.checkedSliderWidth / 2)
    }

    @SuppressLint("RestrictedApi")
    private fun drawScaleSlider(canvas: Canvas) {
        val currentPosition = mIndicatorOptions.currentPosition
        val slideProgress = mIndicatorOptions.sliderProgress
        val coordinateX =
            IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, currentPosition)
        val coordinateY = IndicatorUtils.getCoordinateY(maxWidth)
        if (slideProgress < 1) {
            val evaluate = argbEvaluator?.evaluate(
                slideProgress,
                mIndicatorOptions.checkedSliderColor,
                mIndicatorOptions.normalSliderColor
            )
            mPaint.color = evaluate as Int
            val radius =
                mIndicatorOptions.checkedSliderWidth / 2 - (mIndicatorOptions.checkedSliderWidth / 2 - mIndicatorOptions.normalSliderWidth / 2) * slideProgress
            drawCircle(canvas, coordinateX, coordinateY, radius)
        }

        if (currentPosition == mIndicatorOptions.pageSize - 1) {
            val evaluate = argbEvaluator?.evaluate(
                slideProgress,
                mIndicatorOptions.normalSliderColor,
                mIndicatorOptions.checkedSliderColor
            )
            mPaint.color = evaluate as Int
            val nextCoordinateX = maxWidth / 2
            val nextRadius = minWidth / 2 + (maxWidth / 2 - minWidth / 2) * slideProgress
            drawCircle(canvas, nextCoordinateX, coordinateY, nextRadius)
        } else {
            if (slideProgress > 0) {
                val evaluate = argbEvaluator?.evaluate(
                    slideProgress,
                    mIndicatorOptions.normalSliderColor,
                    mIndicatorOptions.checkedSliderColor
                )
                mPaint.color = evaluate as Int
                val nextCoordinateX =
                    coordinateX + mIndicatorOptions.sliderGap + mIndicatorOptions.normalSliderWidth
                val nextRadius =
                    mIndicatorOptions.normalSliderWidth / 2 + (mIndicatorOptions.checkedSliderWidth / 2 - mIndicatorOptions.normalSliderWidth / 2) * slideProgress
                drawCircle(canvas, nextCoordinateX, coordinateY, nextRadius)
            }
        }
    }

    private fun drawCircleSlider(canvas: Canvas) {
        val currentPosition = mIndicatorOptions.currentPosition
        val startCoordinateX =
            IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, currentPosition)
        val endCoordinateX = IndicatorUtils.getCoordinateX(
            mIndicatorOptions,
            maxWidth,
            (currentPosition + 1) % mIndicatorOptions.pageSize
        )
        val coordinateX =
            startCoordinateX + (endCoordinateX - startCoordinateX) * mIndicatorOptions.sliderProgress
        val coordinateY = IndicatorUtils.getCoordinateY(maxWidth)
        val radius = mIndicatorOptions.checkedSliderWidth / 2
        drawCircle(canvas, coordinateX, coordinateY, radius)
    }

    private fun drawWormSlider(canvas: Canvas) {
        val sliderHeight = mIndicatorOptions.normalSliderWidth
        val slideProgress = mIndicatorOptions.sliderProgress
        val currentPosition = mIndicatorOptions.currentPosition
        val distance = mIndicatorOptions.sliderGap + mIndicatorOptions.normalSliderWidth
        val startCoordinateX =
            IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, currentPosition)
        val left = startCoordinateX + (distance * (slideProgress - 0.5f) * 2.0f).coerceAtLeast(0f)
        mIndicatorOptions.normalSliderWidth / 2 + INDICATOR_PADDING
        val right = startCoordinateX + (distance * slideProgress * 2f).coerceAtLeast(distance) +
                mIndicatorOptions.normalSliderWidth / 2 + INDICATOR_PADDING
        rectF.set(left, INDICATOR_PADDING.toFloat(), right, sliderHeight + INDICATOR_PADDING)
        canvas.drawRoundRect(rectF, sliderHeight, sliderHeight, mPaint)
    }

    private fun drawCircle(canvas: Canvas, coordinateX: Float, coordinateY: Float, radius: Float) {
        canvas.drawCircle(
            coordinateX + INDICATOR_PADDING,
            coordinateY + INDICATOR_PADDING,
            radius,
            mPaint
        )
    }
}