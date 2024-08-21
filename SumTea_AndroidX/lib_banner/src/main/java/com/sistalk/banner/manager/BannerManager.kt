package com.sistalk.banner.manager

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.PageTransformer
import com.sistalk.banner.controller.AttributeController
import com.sistalk.banner.options.BannerOptions
import com.sistalk.banner.transform.OverlapPageTransformer
import com.sistalk.banner.transform.ScaleInTransformer

/**
 * Banner管理类
 * */
class BannerManager {
    private var mBannerOptions: BannerOptions = BannerOptions()

    private var mAttributeController: AttributeController? = null

    private var mCompositePageTransformer: CompositePageTransformer? = null

    private var mMarginPageTransformer: MarginPageTransformer? = null

    private var mDefaultPageTransformer: ViewPager2.PageTransformer? = null

    init {
        mAttributeController = AttributeController(mBannerOptions)
        mCompositePageTransformer = CompositePageTransformer()
    }

    fun getBannerOptions(): BannerOptions {
        return mBannerOptions
    }

    fun initAttrs(context: Context?, attrs: AttributeSet?) {
        mAttributeController?.init(context, attrs)
    }

    fun getCompositePageTransformer(): CompositePageTransformer? {
        return mCompositePageTransformer
    }

    fun addTransformer(transformer: PageTransformer) {
        mCompositePageTransformer?.addTransformer(transformer)
    }

    fun removeTransformer(transformer: PageTransformer) {
        mCompositePageTransformer?.removeTransformer(transformer)
    }

    fun removeMarginPageTransformer() {
        mMarginPageTransformer?.let {
            mCompositePageTransformer?.removeTransformer(it)
        }
    }

    fun removeDefaultPageTransformer() {
        if (mDefaultPageTransformer != null) {
            mCompositePageTransformer!!.removeTransformer(mDefaultPageTransformer!!)
        }
    }

    fun setPageMargin(pageMargin: Int) {
        mBannerOptions.setPageMargin(pageMargin)
    }

    fun createMarginTransformer() {
        removeMarginPageTransformer()
        mMarginPageTransformer = MarginPageTransformer(mBannerOptions.getPageMargin())
        mMarginPageTransformer?.let {
            mCompositePageTransformer?.addTransformer(it)
        }
    }

    fun setMultiPageStyle(overlap: Boolean, scale: Float) {
        removeMarginPageTransformer()
        mDefaultPageTransformer = if (overlap) {
            OverlapPageTransformer(mBannerOptions.getOrientation(), scale, 0f, 1f, 0f)
        } else {
            ScaleInTransformer(scale)
        }
        mDefaultPageTransformer?.let {
            mCompositePageTransformer?.addTransformer(it)
        }
    }
}