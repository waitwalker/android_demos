package com.sistalk.main.banner

import android.content.Context
import android.util.AttributeSet
import com.sistalk.banner.BannerViewPager
import com.sistalk.common.holder.BannerImageHolder
import com.sistalk.common.model.Banner

class HomeBannerView @JvmOverloads constructor(
    context:Context,
    attrs:AttributeSet? = null,
): BannerViewPager<Banner, BannerImageHolder>(context,attrs) {
    val mAdapter = HomeBannerAdapter()
}