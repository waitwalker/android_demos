package com.sistalk.banner.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.sistalk.banner.annotation.AIndicatorSlideMode
import com.sistalk.banner.annotation.AIndicatorStyle
import com.sistalk.banner.indicator.IIndicator
import com.sistalk.banner.mode.IndicatorSlideMode
import com.sistalk.banner.mode.IndicatorStyle
import com.sistalk.banner.options.IndicatorOptions

@Suppress("UNUSED")
open class BaseIndicatorView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : View(context, attrs, defStyleAttr), IIndicator {
    var mIndicatorOptions: IndicatorOptions = IndicatorOptions()

    private var mViewPager: ViewPager? = null
    private var mViewPager2: ViewPager2? = null

    private val mOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            this@BaseIndicatorView.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            this@BaseIndicatorView.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            this@BaseIndicatorView.onPageScrollStateChanged(state)
        }
    }

    init {
        mIndicatorOptions = IndicatorOptions()
    }

    override fun onPageSelected(position: Int) {
        if (getSlideMode() == IndicatorSlideMode.NORMAL) {
            setCurrentPosition(position)
            setSlideProgress(0f)
            invalidate()
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (getSlideMode() != IndicatorSlideMode.NORMAL && getPageSize() > 1) {
            scrollSlider(position, positionOffset)
            invalidate()
        }
    }

    private fun scrollSlider(position: Int, positionOffset: Float) {
        if (mIndicatorOptions.slideMode == IndicatorSlideMode.SCALE
            || mIndicatorOptions.slideMode == IndicatorSlideMode.COLOR
        ) {
            setCurrentPosition(position)
            setSlideProgress(positionOffset)
        } else {
            if (position % getPageSize() == getPageSize() - 1) {
                if (positionOffset < 0.5) {
                    setCurrentPosition(position)
                    setSlideProgress(0f)
                } else {
                    setCurrentPosition(0)
                    setSlideProgress(0f)
                }
            } else {
                setCurrentPosition(position)
                setSlideProgress(positionOffset)
            }
        }
    }

    override fun notifyDataChanged() {
        setupViewPager()
        requestLayout()
        invalidate()
    }


    private fun setupViewPager() {
        mViewPager?.let {
            mViewPager?.removeOnPageChangeListener(this)
            mViewPager?.addOnPageChangeListener(this)
            mViewPager?.adapter?.let {
                setPageSize(it.count)
            }
        }

        mViewPager2?.let {
            mViewPager2?.unregisterOnPageChangeCallback(mOnPageChangeCallback)
            mViewPager2?.registerOnPageChangeCallback(mOnPageChangeCallback)
            mViewPager2?.adapter?.let {
                setPageSize(it.itemCount)
            }
        }
    }

    fun getNormalSlideWidth(): Float {
        return mIndicatorOptions.normalSliderWidth
    }

    fun setNormalSlideWidth(normalSlideWidth: Float) {
        mIndicatorOptions.normalSliderWidth = normalSlideWidth
    }

    fun getCheckedSlideWidth(): Float {
        return mIndicatorOptions.checkedSliderWidth
    }

    fun setCheckedSlideWidth(checkedSliderWidth: Float) {
        mIndicatorOptions.checkedSliderWidth = checkedSliderWidth
    }

    val checkedSliderWidth: Float
        get() = mIndicatorOptions.checkedSliderWidth

    private fun setCurrentPosition(currentPosition: Int) {
        mIndicatorOptions.currentPosition = currentPosition
    }

    fun getCurrentPosition(): Int {
        return mIndicatorOptions.currentPosition
    }

    fun getIndicatorGap():Float {
        return mIndicatorOptions.sliderGap
    }

    fun setIndicatorGap(indicatorGap:Float) {
        mIndicatorOptions.sliderGap = indicatorGap
    }

    fun setCheckedColor(@ColorInt checkedColor:Int) {
        mIndicatorOptions.checkedSliderColor = checkedColor
    }

    fun getCheckedColor():Int {
        return mIndicatorOptions.checkedSliderColor
    }

    fun setNormalColor(@ColorInt normalColor:Int) {
        mIndicatorOptions.normalSliderColor = normalColor
    }

    fun getSlideProgress(): Float {
        return mIndicatorOptions.sliderProgress
    }

    private fun setSlideProgress(slideProgress: Float) {
        mIndicatorOptions.sliderProgress = slideProgress
    }

    fun getPageSize(): Int {
        return mIndicatorOptions.pageSize
    }

    private fun setPageSize(pageSize: Int): BaseIndicatorView {
        mIndicatorOptions.pageSize = pageSize
        return this
    }

    fun setSliderColor(@ColorInt normalColor: Int, @ColorInt checkedColor: Int):BaseIndicatorView {
        mIndicatorOptions.setSliderColor(normalColor,checkedColor)
        return this
    }

    fun setSliderWidth(sliderWidth:Float):BaseIndicatorView {
        mIndicatorOptions.setSliderWidth(sliderWidth)
        return this
    }

    fun setSliderWidth(normalSlideWidth: Float,selectedSliderWidth:Float):BaseIndicatorView {
        mIndicatorOptions.setSliderWidth(normalSlideWidth,selectedSliderWidth)
        return this
    }

    fun setSlideGap(sliderGap:Float):BaseIndicatorView {
        mIndicatorOptions.sliderGap = sliderGap
        return this
    }

    private fun getSlideMode(): Int {
        return mIndicatorOptions.slideMode
    }

    fun setSlideMode(@AIndicatorSlideMode slideMode: Int): BaseIndicatorView {
        mIndicatorOptions.slideMode = slideMode
        return this
    }

    fun setIndicatorStyle(@AIndicatorStyle indicatorStyle: Int):BaseIndicatorView {
        mIndicatorOptions.indicatorStyle = indicatorStyle
        return this
    }

    fun setSliderHeight(sliderHeight:Float):BaseIndicatorView {
        mIndicatorOptions.sliderHeight = sliderHeight
        return this
    }

    fun setupWithViewPager(viewPager: ViewPager) {
        mViewPager = viewPager
        notifyDataChanged()
    }

    fun setupWithViewPager(viewPager2: ViewPager2) {
        mViewPager2 = viewPager2
        notifyDataChanged()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }


    override fun setIndicatorOptions(options:IndicatorOptions) {

    }


}