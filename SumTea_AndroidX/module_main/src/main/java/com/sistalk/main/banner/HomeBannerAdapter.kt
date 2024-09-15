package com.sistalk.main.banner

import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.sistalk.banner.base.BaseBannerAdapter
import com.sistalk.common.holder.BannerImageHolder
import com.sistalk.common.model.Banner
import com.sistalk.glide.setUrl

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
        if (position == 0 && !isRecord) {
            isRecord = true

            holder.imageView.viewTreeObserver.addOnPreDrawListener(object :ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    onFirstFrameTimeCall?.invoke()
                    holder.itemView.viewTreeObserver.removeOnPreDrawListener(this)
                    return false
                }

            })
        }
    }
}