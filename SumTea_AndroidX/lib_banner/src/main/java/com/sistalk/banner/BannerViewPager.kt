package com.sistalk.banner

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.Gravity.END
import android.view.Gravity.START
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.sistalk.banner.annotation.AIndicatorGravity
import com.sistalk.banner.annotation.AIndicatorSlideMode
import com.sistalk.banner.annotation.AIndicatorStyle
import com.sistalk.banner.annotation.APageStyle
import com.sistalk.banner.annotation.Visibility
import com.sistalk.banner.base.BaseBannerAdapter
import com.sistalk.banner.base.BaseBannerAdapter.Companion.MAX_VALUE
import com.sistalk.banner.options.BannerOptions.Companion.DEFAULT_REVEAL_WIDTH
import com.sistalk.banner.base.BaseViewHolder
import com.sistalk.banner.drawer.RoundRectDrawer
import com.sistalk.banner.indicator.IIndicator
import com.sistalk.banner.indicator.IndicatorView
import com.sistalk.banner.manager.BannerManager
import com.sistalk.banner.manager.ReflectLayoutManager
import com.sistalk.banner.mode.PageStyle
import com.sistalk.banner.options.BannerOptions
import com.sistalk.banner.options.IndicatorOptions
import com.sistalk.banner.transform.ScaleInTransformer.Companion.DEFAULT_MIN_SCALE
import com.sistalk.banner.utils.BannerUtils
import com.sistalk.banner.utils.BannerUtils.getOriginalPosition
import com.sistalk.banner.utils.BannerUtils.getRealPosition
import com.sistalk.framework.utils.ViewUtils
import kotlin.math.abs

open class BannerViewPager<T, H : BaseViewHolder<T>> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RelativeLayout(context, attrs), LifecycleObserver {
    private var currentPosition = 0

    private var isCustomIndicator = false

    private var isLooping = false

    private var mOnPageClickListener: OnPageClickListener? = null

    private var mIndicatorView: IIndicator? = null

    private var mIndicatorLayout: RelativeLayout? = null

    private var mViewPager: ViewPager2? = null

    private var mBannerManager: BannerManager = BannerManager()

    private val mHandler = Handler(Looper.getMainLooper())

    private var mBannerPagerAdapter: BaseBannerAdapter<T, H>? = null

    private var onPageChangeCallback: OnPageChangeCallback? = null

    private val mRunnable = Runnable { handlePosition() }

    private var mRadiusRectF: RectF? = null
    private var mRadiusPath: Path? = null

    private var startX = 0
    private var startY = 0

    private val mOnPageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            pageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (getGlobalVisibleRect(Rect()) && windowVisibility == View.VISIBLE) {
                pageSelected(position)
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            pageScrollStateChanged(state)
        }
    }

    init {
        mBannerManager.initAttrs(context, attrs)
        initView()
    }

    private fun initView() {
        mViewPager = ViewPager2(context).apply {
            getChildAt(0).apply {
                clipToPadding = parentClipToPadding()
                clipChildren = parentClipToPadding()
            }
        }

        addView(
            mViewPager, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        mIndicatorLayout = RelativeLayout(context)

        addView(
            mIndicatorLayout,
            LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(ALIGN_PARENT_BOTTOM)
            }
        )

        mViewPager?.setPageTransformer(mBannerManager.getCompositePageTransformer())
        clipChildren = parentClipToPadding()
        clipToPadding = parentClipToPadding()
    }

    /// 被父视图移除
    override fun onDetachedFromWindow() {
        if (mBannerManager.getBannerOptions().isStopLoopWhenDetachedFromWindow()) {
            stopLoop()
        }
        super.onDetachedFromWindow()
        if (context is AppCompatActivity) {
            (context as AppCompatActivity).lifecycle.removeObserver(this)
        }
    }

    /// 被添加到父视图上
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mBannerManager.getBannerOptions().isStopLoopWhenDetachedFromWindow()) {
            startLoop()
        }
        if (context is AppCompatActivity) {
            (context as AppCompatActivity).lifecycle.addObserver(this)
        }
    }

    /// 事件分发
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                isLooping = true
                stopLoop()
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                isLooping = false
                startLoop()
            }

            else -> {

            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val doNotNeedIntercept =
            (mViewPager?.isUserInputEnabled != true || (mBannerPagerAdapter != null && (mBannerPagerAdapter?.getData()?.size
                ?: 0) <= 1))
        if (doNotNeedIntercept) {
            return super.onInterceptTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x.toInt()
                startY = ev.y.toInt()
                parent.requestDisallowInterceptTouchEvent(
                    !mBannerManager.getBannerOptions().isDisallowParentInterceptDownEvent()
                )
            }

            MotionEvent.ACTION_MOVE -> {
                val endX = ev.x.toInt()
                val endY = ev.y.toInt()
                val disX = abs(endX - startX)
                val disY: Int = abs(endY - startY)
                val orientation: Int = mBannerManager.getBannerOptions().getOrientation()
                if (orientation == ViewPager2.ORIENTATION_VERTICAL) {
                    onVerticalActionMove(endY, disX, disY)
                } else if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    onHorizontalActionMove(endX, disX, disY)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent.requestDisallowInterceptTouchEvent(
                false
            )

            MotionEvent.ACTION_OUTSIDE -> {}
            else -> {}
        }

        return super.onInterceptTouchEvent(ev)
    }

    private fun onVerticalActionMove(endY: Int, disX: Int, disY: Int) {
        if (disY > disX) {
            val canLoop: Boolean = mBannerManager.getBannerOptions().isCanLoop()
            if (!canLoop) {
                if (currentPosition == 0 && endY - startY > 0) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    parent.requestDisallowInterceptTouchEvent(
                        currentPosition != getData().size - 1 || endY - startY >= 0
                    )
                }
            } else {
                parent.requestDisallowInterceptTouchEvent(true)
            }
        } else if (disX > disY) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

    private fun onHorizontalActionMove(endX: Int, disX: Int, disY: Int) {
        if (disX > disY) {
            val canLoop = mBannerManager.getBannerOptions().isCanLoop()
            if (!canLoop) {
                if (currentPosition == 0 && endX - startX > 0) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    parent.requestDisallowInterceptTouchEvent(
                        (currentPosition != getData().size - 1) || endX - startX >= 0
                    )
                }
            } else {
                parent.requestDisallowInterceptTouchEvent(true)
            }
        } else if (disY > disX) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

    private fun pageScrollStateChanged(state: Int) {
        mIndicatorView?.onPageScrollStateChanged(state)
        onPageChangeCallback?.onPageScrollStateChanged(state)
    }

    // 选中
    private fun pageSelected(position: Int) {
        val size = mBannerPagerAdapter?.getListSize() ?: 0
        val canLoop = mBannerManager.getBannerOptions().isCanLoop()
        currentPosition = getRealPosition(position, size)
        val needResetCurrentItem =
            (size > 0 && canLoop && (position == 0 || position == MAX_VALUE - 1))
        if (needResetCurrentItem) {
            resetCurrentItem(currentPosition)
        }
        onPageChangeCallback?.onPageSelected(currentPosition)
        mIndicatorView?.onPageSelected(currentPosition)
    }

    private fun pageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val listSize = mBannerPagerAdapter?.getListSize() ?: 0
        val realPosition = getRealPosition(position, listSize)
        if (listSize > 0) {
            onPageChangeCallback?.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
            mIndicatorView?.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
        }
    }

    private fun handlePosition() {
        if ((mBannerPagerAdapter != null) && ((mBannerPagerAdapter?.getListSize()
                ?: 0) > 1) && isAutoPlay()
        ) {
            var currentItem = (mViewPager?.currentItem ?: 0) + 1
            if (currentItem >= (mViewPager?.adapter?.itemCount ?: MAX_VALUE)) {
                currentItem = 0
            }
            mViewPager?.currentItem = currentItem
            mHandler.postDelayed(mRunnable, getInterval())
        }
    }

    private fun initBannerData(isInitCurrent: Boolean = true) {
        val list: List<T>? = mBannerPagerAdapter?.getData()
        if (list != null) {
            setIndicatorValues(list)
            setupViewPager(list, isInitCurrent)
            initRoundCorner()
        }
    }

    private fun setIndicatorValues(list: List<T>) {
        val bannerOptions: BannerOptions = mBannerManager.getBannerOptions()
        mIndicatorLayout?.visibility = bannerOptions.getIndicatorVisibility()
        bannerOptions.resetIndicatorOptions()
        if (!isCustomIndicator || mIndicatorView == null) {
            mIndicatorView = IndicatorView(context)
        }
        initIndicator(bannerOptions.getIndicatorOptions(), list)
    }

    private fun initIndicator(indicatorOptions: IndicatorOptions, list: List<T>) {
        if ((mIndicatorView is View) && (mIndicatorView as View?)?.parent == null) {
            mIndicatorLayout?.removeAllViews()
            mIndicatorLayout?.addView(mIndicatorView as View?)
            initIndicatorSliderMargin()
            initIndicatorGravity()
        }
        mIndicatorView?.setIndicatorOptions(indicatorOptions)
        indicatorOptions.pageSize = list.size
        mIndicatorView?.notifyDataChanged()
    }

    private fun initIndicatorGravity() {
        val layoutParams = (mIndicatorView as View?)?.layoutParams as LayoutParams
        when (mBannerManager.getBannerOptions().getIndicatorGravity()) {
            CENTER -> layoutParams.addRule(CENTER_HORIZONTAL)
            START -> layoutParams.addRule(ALIGN_PARENT_LEFT)
            END -> layoutParams.addRule(ALIGN_PARENT_RIGHT)
            else -> {}
        }
    }

    private fun initIndicatorSliderMargin() {
        val layoutParams = (mIndicatorView as View?)?.layoutParams as MarginLayoutParams
        val indicatorMargin = mBannerManager.getBannerOptions().getIndicatorMargin()
        if (indicatorMargin == null) {
            val dp10: Int = BannerUtils.dp2px(10f)
            layoutParams.setMargins(dp10, dp10, dp10, dp10)
        } else {
            layoutParams.setMargins(
                indicatorMargin.left,
                indicatorMargin.top,
                indicatorMargin.right,
                indicatorMargin.bottom
            )
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        val roundRectRadiusArray: FloatArray? =
            mBannerManager.getBannerOptions().getRoundRectRadiusArray()
        if (mRadiusRectF != null && mRadiusPath != null && roundRectRadiusArray != null) {
            mRadiusRectF?.right = this.width.toFloat()
            mRadiusRectF?.bottom = this.height.toFloat()
            mRadiusPath?.addRoundRect(mRadiusRectF!!, roundRectRadiusArray, Path.Direction.CW)
            canvas.clipPath(mRadiusPath!!)
        }
        super.dispatchDraw(canvas)
    }

    private fun initRoundCorner() {
        val roundCorner = mBannerManager.getBannerOptions().getRoundRectRadius()
        if (roundCorner > 0) {
            ViewUtils.setClipViewCornerRadius(this, roundCorner)
        }
    }

    @SuppressLint("WrongConstant")
    private fun setupViewPager(list: List<T>, isInitCurrent: Boolean = true) {
        if (mBannerPagerAdapter == null) {
            throw NullPointerException("You must set adapter for BannerViewPager")
        }
        val bannerOptions = mBannerManager.getBannerOptions()
        if (bannerOptions.getScrollDuration() != 0) {
            mViewPager?.let {
                ReflectLayoutManager.reflectLayoutManager(it, bannerOptions.getScrollDuration())
            }
        }

        mBannerPagerAdapter?.setCanLoop(bannerOptions.isCanLoop())
        mBannerPagerAdapter?.setPageClickListener(mOnPageClickListener)
        mViewPager?.adapter = mBannerPagerAdapter
        if (isCanLoopSafely()) {
            mViewPager?.setCurrentItem(getOriginalPosition(list.size) + currentPosition, false)
        }
        mViewPager?.unregisterOnPageChangeCallback(mOnPageChangeCallback)
        mViewPager?.unregisterOnPageChangeCallback(mOnPageChangeCallback)
        mViewPager?.orientation = bannerOptions.getOrientation()
        val limit = bannerOptions.getOffScreenPageLimit()
        mViewPager?.offscreenPageLimit = if (limit > 0) limit else OFFSCREEN_PAGE_LIMIT_DEFAULT
        initRevealWidth(bannerOptions)
        initPageStyle(bannerOptions.getPageStyle())
        stopLoop()
        startLoop()
    }

    private fun initRevealWidth(bannerOptions: BannerOptions) {
        val rightRevealWidth = bannerOptions.getRightRevealWidth()
        val leftRevealWidth = bannerOptions.getLeftRevealWidth()
        if (leftRevealWidth != DEFAULT_REVEAL_WIDTH || rightRevealWidth != DEFAULT_REVEAL_WIDTH) {
            val recyclerView = mViewPager?.getChildAt(0)
            if (recyclerView is RecyclerView) {
                val orientation = bannerOptions.getOrientation()
                val padding2 = bannerOptions.getPageMargin() + rightRevealWidth
                val padding1 = bannerOptions.getPageMargin() + leftRevealWidth
                if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    recyclerView.setPadding(padding1, 0, padding2, 0)
                } else if (orientation == ViewPager2.ORIENTATION_VERTICAL) {
                    recyclerView.setPadding(0, padding1, 0, padding2)
                }
                recyclerView.clipToPadding = false
            }
        }
        mBannerManager.createMarginTransformer()
    }

    fun refreshRevealWidth() {
        initRevealWidth(mBannerManager.getBannerOptions())
    }

    private fun initPageStyle(@APageStyle pageStyle: Int) {
        val pageScale = mBannerManager.getBannerOptions().getPageScale()
        if (pageStyle == PageStyle.MULTI_PAGE_OVERLAP) {
            mBannerManager.setMultiPageStyle(true, pageScale)
        } else if (pageStyle == PageStyle.MULTI_PAGE_SCALE) {
            mBannerManager.setMultiPageStyle(false, pageScale)
        }
    }

    private fun resetCurrentItem(item: Int) {
        if (isCanLoopSafely()) {
            mViewPager?.setCurrentItem(
                getOriginalPosition(
                    mBannerPagerAdapter?.getListSize() ?: 0
                ) + item, false
            )
        } else {
            mViewPager?.setCurrentItem(item, false)
        }
    }

    private fun refreshIndicator(data: List<T>) {
        setIndicatorValues(data)
        mBannerManager.getBannerOptions().getIndicatorOptions().currentPosition =
            getRealPosition(mViewPager?.currentItem ?: 0, data.size)
        mIndicatorView?.notifyDataChanged()
    }

    private val KEY_SUPER_STATE = "SUPER_STATE"
    private val KEY_CURRENT_POSITION = "CURRENT_POSITION"
    private val KEY_IS_CUSTOM_INDICATOR = "IS_CUSTOM_INDICATOR"


    private fun getInterval(): Long {
        return mBannerManager.getBannerOptions().getInterval()
    }

    private fun isAutoPlay(): Boolean {
        return mBannerManager.getBannerOptions().isAutoPlay()
    }

    private fun isCanLoopSafely(): Boolean {
        return (mBannerManager.getBannerOptions().isCanLoop() && (mBannerPagerAdapter?.getListSize()
            ?: 0) > 1)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable(KEY_SUPER_STATE, superState)
        bundle.putInt(KEY_CURRENT_POSITION, currentPosition)
        bundle.putBoolean(KEY_IS_CUSTOM_INDICATOR, isCustomIndicator)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        val superState = bundle.getParcelable<Parcelable>(KEY_SUPER_STATE)
        super.onRestoreInstanceState(superState)
        currentPosition = bundle.getInt(KEY_CURRENT_POSITION)
        isCustomIndicator = bundle.getBoolean(KEY_IS_CUSTOM_INDICATOR)
        setCurrentItem(currentPosition, false)
    }

    open fun parentClipToPadding(): Boolean = true

    fun getData(): List<T> {
        return mBannerPagerAdapter?.getData() ?: emptyList()
    }

    fun startLoop() {
        if (!isLooping && isAutoPlay() && (mBannerPagerAdapter != null) && (mBannerPagerAdapter?.getListSize()
                ?: 0) > 1
        ) {
            mHandler.postDelayed(mRunnable, getInterval())
            isLooping = true
        }
    }

    fun startLoopNow() {
        if (!isLooping && isAutoPlay() && mBannerPagerAdapter != null && (mBannerPagerAdapter?.getListSize()
                ?: 0) > 1
        ) {
            mHandler.post(mRunnable)
            isLooping = true
        }
    }

    fun stopLoop() {
        if (isLooping) {
            mHandler.removeCallbacks(mRunnable)
            isLooping = false
        }
    }

    fun setAdapter(adapter: BaseBannerAdapter<T, H>): BannerViewPager<T, H> {
        mBannerPagerAdapter = adapter
        return this
    }

    fun getAdapter(): BaseBannerAdapter<T, H>? {
        return mBannerPagerAdapter
    }

    fun setRoundCorner(radius: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setRoundRectRadius(radius)
        return this
    }

    fun setRoundCorner(
        topLeftRadius: Int,
        topRightRadius: Int,
        bottomLeftRadius: Int,
        bottomRightRadius: Int
    ): BannerViewPager<T, H> {
        mRadiusRectF = RectF()
        mRadiusPath = Path()
        mBannerManager.getBannerOptions()
            .setRoundRectRadius(topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius)
        return this
    }

    fun setCanLoop(canLoop: Boolean): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setCanLoop(canLoop)
        if (!canLoop) {
            mBannerManager.getBannerOptions().setAutoPlay(false)
        }
        return this
    }

    fun setInterval(interval: Long): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setInterval(interval)
        return this
    }

    fun setPageTransformer(transformer: ViewPager2.PageTransformer?): BannerViewPager<T, H> {
        if (transformer != null) {
            mViewPager?.setPageTransformer(transformer)
        }
        return this
    }

    fun addPageTransformer(transformer: ViewPager2.PageTransformer?): BannerViewPager<T, H> {
        if (transformer != null) {
            mBannerManager.addTransformer(transformer)
        }
        return this
    }

    fun removeTransformer(transformer: ViewPager2.PageTransformer?) {
        if (transformer != null) {
            mBannerManager.removeTransformer(transformer)
        }
    }

    fun removeDefaultPageTransformer() {
        mBannerManager.removeDefaultPageTransformer()
    }

    fun removeMarginPageTransformer() {
        mBannerManager.removeMarginPageTransformer()
    }

    fun setPageMargin(pageMargin: Int): BannerViewPager<T, H> {
        mBannerManager.setPageMargin(pageMargin)
        return this
    }

    fun setOnPageClickListener(onPageClickListener: OnPageClickListener?): BannerViewPager<T, H> {
        mOnPageClickListener = onPageClickListener
        return this
    }

    fun setScrollDuration(scrollDuration: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setScrollDuration(scrollDuration)
        return this
    }

    fun setIndicatorSlideColor(
        @ColorInt normalColor: Int,
        @ColorInt checkedColor: Int
    ): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setIndicatorSliderColor(normalColor, checkedColor)
        return this
    }

    fun setIndicatorSliderRadius(radius: Int): BannerViewPager<T, H> {
        setIndicatorSliderRadius(radius, radius)
        return this
    }

    fun setIndicatorSliderRadius(normalRadius: Int, checkedRadius: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions()
            .setIndicatorSliderWidth(normalRadius * 2, checkedRadius * 2)
        return this
    }

    fun setIndicatorSliderWidth(indicatorWidth: Int): BannerViewPager<T, H> {
        setIndicatorSliderWidth(indicatorWidth, indicatorWidth)
        return this
    }

    fun setIndicatorSliderWidth(normalWidth: Int, checkedWidth: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setIndicatorSliderWidth(normalWidth, checkedWidth)
        return this
    }

    fun setIndicatorHeight(indicatorHeight: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setIndicatorHeight(indicatorHeight)
        return this
    }

    fun setIndicatorSliderGap(indicatorGap: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setIndicatorGap(indicatorGap.toFloat())
        return this
    }

    fun setIndicatorVisibility(@Visibility visibility: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setIndicatorVisibility(visibility)
        return this
    }

    fun setIndicatorGravity(@AIndicatorGravity gravity: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setIndicatorGravity(gravity)
        return this
    }

    fun setIndicatorSlideMode(@AIndicatorSlideMode slideMode: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setIndicatorSlideMode(slideMode)
        return this
    }

    fun setIndicatorView(customIndicator: IIndicator?): BannerViewPager<T, H> {
        if (customIndicator is View) {
            isCustomIndicator = true
            mIndicatorView = customIndicator
        }
        return this
    }

    fun setIndicatorStyle(@AIndicatorStyle indicatorStyle: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setIndicatorStyle(indicatorStyle)
        return this
    }

    fun create(data: List<T>?, isInitCurrent: Boolean = true) {
        if (mBannerPagerAdapter == null) {
            throw NullPointerException("You must set adapter for BannerViewPager")
        }
        mBannerPagerAdapter?.setData(data)
        initBannerData(isInitCurrent)
    }

    fun create() {
        create(ArrayList())
    }

    fun setOrientation(@ViewPager2.Orientation orientation: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setOrientation(orientation)
        return this
    }

    fun addItemDecoration(decor: ItemDecoration, index: Int) {
        if (isCanLoopSafely()) {
            val pageSize = mBannerPagerAdapter?.getListSize() ?: 0
            val currentItem = mViewPager?.currentItem ?: 0
            val realPosition = BannerUtils.getRealPosition(currentItem, pageSize)
            if (currentItem != index) {
                if (index == 0 && realPosition == pageSize - 1) {
                    mViewPager?.addItemDecoration(decor, currentItem + 1)
                } else if (realPosition == 0 && index == pageSize - 1) {
                    mViewPager?.addItemDecoration(decor, currentItem - 1)
                } else {
                    mViewPager?.addItemDecoration(decor, currentItem + index - realPosition)
                }
            }
        } else {
            mViewPager?.addItemDecoration(decor, index)
        }
    }

    fun addItemDecoration(decor: ItemDecoration) {
        mViewPager?.addItemDecoration(decor)
    }

    fun refreshData(list: List<T>?) {
        post {
            if (isAttachedToWindow && list != null && mBannerPagerAdapter != null) {
                stopLoop()
                mBannerPagerAdapter?.setData(list)
                val limitSize = mBannerManager.getBannerOptions().getOffScreenPageLimit()
                mViewPager?.offscreenPageLimit =
                    if (limitSize > 0) limitSize else OFFSCREEN_PAGE_LIMIT_DEFAULT
                resetCurrentItem(getCurrentItem())
                refreshIndicator(list)
                startLoop()
            }
        }
    }

    fun getCurrentItem(): Int {
        return currentPosition
    }

    fun setCurrentItem(item: Int) {
        setCurrentItem(item, true)
    }

    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        var item = item
        if (isCanLoopSafely()) {
            val pageSize = mBannerPagerAdapter?.getListSize() ?: 0
            item = if (item >= pageSize) pageSize - 1 else item
            val currentItem = mViewPager?.currentItem ?: 0
            mBannerManager.getBannerOptions().isCanLoop()
            val realPosition = BannerUtils.getRealPosition(currentPosition, pageSize)
            if (currentItem != item) {
                stopLoop()
                if (item == 0 && realPosition == pageSize - 1) {
                    mViewPager?.setCurrentItem(currentItem + 1, smoothScroll)
                } else if (realPosition == 0 && item == pageSize - 1) {
                    mViewPager?.setCurrentItem(currentItem - 1, smoothScroll)
                } else {
                    mViewPager?.setCurrentItem(currentItem + (item - realPosition), smoothScroll)
                }
                stopLoop()
            }
        } else {
            mViewPager?.setCurrentItem(item, smoothScroll)
        }
    }

    fun setPageStyle(@APageStyle pageStyle: Int): BannerViewPager<T, H> {
        setPageStyle(pageStyle, DEFAULT_MIN_SCALE)
        return this
    }

    fun setPageStyle(@APageStyle pageStyle: Int, pageScale: Float): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setPageStyle(pageStyle)
        mBannerManager.getBannerOptions().setPageScale(pageScale)
        return this
    }

    fun setRevealWidth(revealWidth: Int): BannerViewPager<T, H> {
        setRevealWidth(revealWidth, revealWidth)
        return this
    }

    fun setRevealWidth(leftRevealWidth: Int, rightRevealWidth: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setRightRevealWidth(rightRevealWidth)
        mBannerManager.getBannerOptions().setLeftRevealWidth(leftRevealWidth)
        return this
    }

    fun setOffScreenPageLimit(offScreenPageLimit: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setOffScreenPageLimit(offScreenPageLimit)
        return this
    }

    fun setIndicatorMargin(left: Int, top: Int, right: Int, bottom: Int): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setIndicatorMargin(left, top, right, bottom)
        return this
    }

    fun setUserInputEnabled(userInputEnabled: Boolean): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setUserInputEnabled(userInputEnabled)
        return this
    }

    interface OnPageClickListener {
        fun onPageClick(clickedView: View?, position: Int)
    }

    fun registerOnPageChangeCallBack(onPageChangeCallBack: OnPageChangeCallback?): BannerViewPager<T, H> {
        this.onPageChangeCallback = onPageChangeCallBack
        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        stopLoop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        startLoopNow()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        stopLoop()
    }

    fun disallowParentInterceptDownEvent(disallowParentInterceptDownEvent: Boolean): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions()
            .setDisallowParentInterceptDownEvent(disallowParentInterceptDownEvent)
        return this
    }

    fun setRTLMode(rtlMode: Boolean): BannerViewPager<T, H> {
        mViewPager?.layoutDirection = if (rtlMode) LAYOUT_DIRECTION_RTL else LAYOUT_DIRECTION_LTR
        mBannerManager.getBannerOptions().setRtl(rtlMode)
        return this
    }

    fun stopLoopWhenDetachedFromWindow(stopLoopWhenDetachedFromWindow: Boolean): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions()
            .setStopLoopWhenDetachedFromWindow(stopLoopWhenDetachedFromWindow)
        return this
    }

    fun showIndicatorWhenOneItem(showIndicatorWhenOneItem: Boolean): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().showIndicatorWhenOneItem(showIndicatorWhenOneItem)
        return this
    }

    @Deprecated("deprecated")
    fun disallowInterceptTouchEvent(disallowIntercept: Boolean): BannerViewPager<T, H> {
        mBannerManager.getBannerOptions().setDisallowParentInterceptDownEvent(disallowIntercept)
        return this
    }

    fun getCurrentViewHolder(): ViewHolder? {
        mViewPager?.let {
            if (it.childCount > 0) {
                val view = it[0]
                if (view is RecyclerView) {
                    return view.findViewHolderForAdapterPosition(it.currentItem)
                }
            }
        }
        return null
    }


    fun setCurrentPosition(position: Int): BannerViewPager<T, H> {
        currentPosition = position
        return this
    }
}

