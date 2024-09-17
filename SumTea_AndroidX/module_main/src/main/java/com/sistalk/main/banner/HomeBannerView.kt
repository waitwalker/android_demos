package com.sistalk.main.banner

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.sistalk.banner.BannerViewPager
import com.sistalk.common.holder.BannerImageHolder
import com.sistalk.common.model.Banner
import com.sistalk.common.provider.MainServiceProvider
import com.sistalk.framework.utils.dpToPx

class HomeBannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : BannerViewPager<Banner, BannerImageHolder>(context, attrs) {
    val mAdapter = HomeBannerAdapter()

    init {
        setAdapter(mAdapter)
            .setAutoPlay(true)
            .setScrollDuration(500)
            .setCanLoop(true)
            .setInterval(2000L)
            .setIndicatorSliderWidth(dpToPx(6))
            .setIndicatorSlideColor(Color.parseColor("#8F8E94"), Color.parseColor("#0165b8"))
            .create()

        mAdapter.setPageClickListener(object : OnPageClickListener {
            override fun onPageClick(clickedView: View?, position: Int) {
                val item = mAdapter.getData()[position]
                if (!item.url.isNullOrEmpty()) {
                    MainServiceProvider.toArticleDetail(
                        context = context,
                        url = item.url!!,
                        title = item.title ?: ""
                    )
                }
            }
        })
    }

    fun setData(list: MutableList<Banner>) {
        refreshData(list)
    }
}