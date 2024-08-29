package com.sistalk.banner.drawer

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.RectF
import com.sistalk.banner.base.BaseDrawer
import com.sistalk.banner.mode.IndicatorSlideMode
import com.sistalk.banner.options.IndicatorOptions
import com.sistalk.banner.utils.IndicatorUtils
import kotlin.math.min

open class RectDrawer internal constructor(indicatorOptions: IndicatorOptions) :
    BaseDrawer(indicatorOptions) {
    internal var mRectF: RectF = RectF()

    override fun onDraw(canvas: Canvas) {
        val pageSize = mIndicatorOptions.pageSize
        if (pageSize > 1 || mIndicatorOptions.showIndicatorOneItem && pageSize == 1) {
            if (isWidthEquals && mIndicatorOptions.slideMode != IndicatorSlideMode.NORMAL) {
                drawUncheckedSlider(canvas, pageSize)
                drawCheckedSlider(canvas)
            } else {
                if (mIndicatorOptions.slideMode == IndicatorSlideMode.SCALE) {
                    for (i in 0 until pageSize) {
                        drawScaleSlider(canvas, i)
                    }
                } else {
                    drawInequalitySlider(canvas, pageSize)
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun drawScaleSlider(canvas: Canvas, i: Int) {
        val checkedColor = mIndicatorOptions.checkedSliderColor
        val indicatorGap = mIndicatorOptions.sliderGap
        val sliderHeight = mIndicatorOptions.sliderHeight
        val currentPosition = mIndicatorOptions.currentPosition
        val normalWidth = mIndicatorOptions.normalSliderWidth
        val checkedWidth = mIndicatorOptions.checkedSliderWidth
        when {
            i < currentPosition -> {
                mPaint.color = mIndicatorOptions.normalSliderColor
                val left: Float = if (currentPosition == mIndicatorOptions.pageSize - 1) {
                    (i * normalWidth + i * indicatorGap) + (checkedWidth - normalWidth) * mIndicatorOptions.sliderProgress
                } else {
                    (i * normalWidth + i * indicatorGap)
                }
                mRectF.set(left, 0f, left + normalWidth, sliderHeight)
                drawRoundRect(canvas, sliderHeight, sliderHeight)
            }

            i == currentPosition -> {
                mPaint.color = checkedColor
                val slideProgress = mIndicatorOptions.sliderProgress
                if (currentPosition == mIndicatorOptions.pageSize - 1) {
                    val evaluate = argbEvaluator?.evaluate(
                        slideProgress,
                        checkedColor,
                        mIndicatorOptions.normalSliderColor
                    )
                    mPaint.color = (evaluate as Int)
                    val right =
                        (mIndicatorOptions.pageSize - 1) * (normalWidth + mIndicatorOptions.sliderGap) + checkedWidth
                    val left = right - checkedWidth + (checkedWidth - normalWidth) * slideProgress
                    mRectF.set(left, 0f, right, sliderHeight)
                    drawRoundRect(canvas, sliderHeight, sliderHeight)
                } else {
                    if (slideProgress < 1) {
                        val evaluate = argbEvaluator?.evaluate(
                            slideProgress,
                            checkedColor,
                            mIndicatorOptions.normalSliderColor
                        )
                        mPaint.color = (evaluate as Int)
                        val left = i * normalWidth + i * indicatorGap
                        val right =
                            left + normalWidth + (checkedWidth - normalWidth) * (1 - slideProgress)
                        mRectF.set(left, 0f, right, sliderHeight)
                        drawRoundRect(canvas, sliderHeight, sliderHeight)
                    }
                }

                if (currentPosition == mIndicatorOptions.pageSize - 1) {
                    if (slideProgress > 0) {
                        val evaluate = argbEvaluator?.evaluate(
                            1 - slideProgress,
                            checkedColor,
                            mIndicatorOptions.normalSliderColor
                        )
                        mPaint.color = evaluate as Int
                        val left = 0f
                        val right =
                            left + normalWidth + (checkedWidth - normalWidth) * slideProgress
                        mRectF.set(left, 0f, right, sliderHeight)
                        drawRoundRect(canvas, sliderHeight, sliderHeight)
                    }
                } else {
                    if (slideProgress > 0) {
                        val evaluate = argbEvaluator?.evaluate(
                            1 - slideProgress,
                            checkedColor,
                            mIndicatorOptions.normalSliderColor
                        )
                        mPaint.color = evaluate as Int
                        val right =
                            i * normalWidth + i * indicatorGap + normalWidth + (indicatorGap + checkedWidth)
                        val left =
                            right - (normalWidth) - (checkedWidth - normalWidth) * slideProgress
                        mRectF.set(left, 0f, right, sliderHeight)
                        drawRoundRect(canvas, sliderHeight, sliderHeight)
                    }
                }
            }

            else -> {
                if ((currentPosition + 1 != i || mIndicatorOptions.sliderProgress == 0f)) {
                    mPaint.color = mIndicatorOptions.normalSliderColor
                    val left = i * minWidth + i * indicatorGap + (checkedWidth - minWidth)
                    mRectF.set(left, 0f, left + minWidth, sliderHeight)
                    drawRoundRect(canvas, sliderHeight, sliderHeight)
                }
            }
        }
    }

    private fun drawUncheckedSlider(canvas: Canvas, pageSize: Int) {
        for (i in 0 until pageSize) {
            mPaint.color = mIndicatorOptions.normalSliderColor
            val left = i * maxWidth + i * mIndicatorOptions.sliderGap + (maxWidth - minWidth)
            mRectF.set(left, 0f, left + minWidth, mIndicatorOptions.sliderHeight)
            drawRoundRect(canvas, mIndicatorOptions.sliderHeight, mIndicatorOptions.sliderHeight)
        }
    }

    private fun drawInequalitySlider(canvas: Canvas, pageSize: Int) {
        var left = 0f
        for (i in 0 until pageSize) {
            val sliderWidth = (if (i == mIndicatorOptions.currentPosition) maxWidth else minWidth)
            mPaint.color =
                if ((i == mIndicatorOptions.currentPosition)) mIndicatorOptions.checkedSliderColor
                else mIndicatorOptions.normalSliderColor
            mRectF.set(left,0f,left + sliderWidth, mIndicatorOptions.sliderHeight)
            drawRoundRect(canvas,mIndicatorOptions.sliderHeight,mIndicatorOptions.sliderHeight)
            left += sliderWidth + mIndicatorOptions.sliderGap
        }
    }

    private fun drawCheckedSlider(canvas: Canvas) {
        mPaint.color = mIndicatorOptions.checkedSliderColor
        when(mIndicatorOptions.slideMode) {
            IndicatorSlideMode.SMOOTH->drawSmoothSldier(canvas)
            IndicatorSlideMode.WORM ->drawWormSlider(canvas)
            IndicatorSlideMode.COLOR->drawColorSlider(canvas)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun drawColorSlider(canvas: Canvas) {
        val currentPosition = mIndicatorOptions.currentPosition
        val slideProgress = mIndicatorOptions.sliderProgress
        val left = currentPosition * minWidth + currentPosition * mIndicatorOptions.sliderGap
        if (slideProgress < 0.99) {
            val evaluate = argbEvaluator?.evaluate(slideProgress,mIndicatorOptions.checkedSliderColor,mIndicatorOptions.normalSliderColor)
            mPaint.color = (evaluate as Int)
            mRectF.set(left,0f,left + minWidth, mIndicatorOptions.sliderHeight)
            drawRoundRect(canvas,mIndicatorOptions.sliderHeight,mIndicatorOptions.sliderHeight)
        }

        var nextSliderLeft = left + mIndicatorOptions.sliderGap + mIndicatorOptions.normalSliderWidth
        if (currentPosition == mIndicatorOptions.pageSize - 1) {
            nextSliderLeft = 0f
        }

        val evaluate = argbEvaluator?.evaluate(1-slideProgress,mIndicatorOptions.checkedSliderColor,mIndicatorOptions.normalSliderColor)
        mPaint.color = evaluate as Int
        mRectF.set(nextSliderLeft,0f,nextSliderLeft + minWidth, mIndicatorOptions.sliderHeight)
        drawRoundRect(canvas,mIndicatorOptions.sliderHeight, mIndicatorOptions.sliderHeight)
    }

    private fun drawWormSlider(canvas: Canvas) {
        val sliderHeight = mIndicatorOptions.sliderHeight
        val sliderProgress = mIndicatorOptions.sliderProgress
        val currentPosition = mIndicatorOptions.currentPosition
        val distance = mIndicatorOptions.sliderGap + mIndicatorOptions.normalSliderWidth
        val startCoordinateX = IndicatorUtils.getCoordinateX(mIndicatorOptions,maxWidth,currentPosition)
        val left = startCoordinateX + (distance * (sliderProgress - 0.5f))
    }

    protected open fun drawRoundRect(canvas: Canvas,rx:Float,ry:Float) {
        drawDash(canvas)
    }

    protected open fun drawDash(canvas: Canvas) {}


}