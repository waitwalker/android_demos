package com.sistalk.banner

import android.content.Context
import android.graphics.Path
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.LifecycleObserver
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.sistalk.banner.base.BaseBannerAdapter
import com.sistalk.banner.base.BaseBannerAdapter.Companion.MAX_VALUE
import com.sistalk.banner.base.BaseViewHolder
import com.sistalk.banner.indicator.IIndicator
import com.sistalk.banner.manager.BannerManager
import com.sistalk.banner.utils.BannerUtils

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
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
        }
    }

    private fun pageSelected(position: Int) {
        val size = mBannerPagerAdapter?.getListSize()?:0
        val canLoop = mBannerManager.getBannerOptions().isCanLoop()
        currentPosition = BannerUtils.getRealPosition(position,size)
        val needResetCurrentItem = (size > 0 && canLoop && (position == 0 || position == MAX_VALUE - 1))
        if (needResetCurrentItem) {

        }
    }

    private fun pageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val listSize = mBannerPagerAdapter?.getListSize() ?: 0
        val realPosition = BannerUtils.getRealPosition(position, listSize)
        if (listSize > 0) {
            onPageChangeCallback?.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
            mIndicatorView?.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
        }
    }

    private fun resetCurrentItem(item:Int) {
        if (isCanLoopSafely()) {
            mViewPager?.setCurrentItem()
        }
    }

    private fun getInterval(): Long {
        return mBannerManager.getBannerOptions().getInterval()
    }

    private fun isAutoPlay(): Boolean {
        return mBannerManager.getBannerOptions().isAutoPlay()
    }

    private fun isCanLoopSafely():Boolean {
        return (mBannerManager.getBannerOptions().isCanLoop() && (mBannerPagerAdapter?.getListSize() ?:0) > 1)
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


    interface OnPageClickListener {
        fun onPageClick(clickedView: View?, position: Int)
    }

}

