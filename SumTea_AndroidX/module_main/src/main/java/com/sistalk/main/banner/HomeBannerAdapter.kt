package com.sistalk.main.banner

import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.sistalk.banner.base.BaseBannerAdapter
import com.sistalk.common.holder.BannerImageHolder
import com.sistalk.common.model.Banner

class HomeBannerAdapter:BaseBannerAdapter<Banner,BannerImageHolder>() {
    var isRecord = false
    var onFirstFrameTimeCall:(()->UInt)? = null
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerImageHolder {
        val imageView = AppCompatImageView(parent.context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        }
        return BannerImageHolder(imageView)
    }

    override fun onBindView(holder: BannerImageHolder, data: Banner, position: Int, pageSize: Int) {
        data.imagePath?.let {
            holder.imageView.setUrl(it)
        }
    }
}