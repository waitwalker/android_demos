package com.sistalk.banner.transform

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.sistalk.framework.utils.dpToPx
import kotlin.math.abs
import kotlin.math.max

class OverlapPageTransformer(
    private val orientation: Int,
    private val minScale: Float = 0f,
    private val unSelectedItemRotation: Float = 0f,
    private val unSelectedItemAlpha: Float = 0f,
    private val itemGap: Float = 0f,
) : ViewPager2.PageTransformer {
    init {
        require(minScale in 0f..1f) {
            "minScale value should be between 0.0 to 1.0"
        }
        require(unSelectedItemAlpha in 0f..1f) {
            "unSelectedItemAlpha value should be between 0.0 to 1.0"
        }
    }

    private val scalingValue = 0.2f
    override fun transformPage(page: View, position: Float) {
        page.apply {
            elevation = -abs(position)
            val delta = max(1f - abs(position * (1 - 0.5f)), 0.5f)
            if (unSelectedItemRotation != 0f) {
                val rotation =
                    (1 - delta) * if (position > 0) unSelectedItemRotation else -unSelectedItemRotation
                rotationY = rotation
            }
            val scaleDelta = abs(position * scalingValue)
            val scale = max(1f - scaleDelta, minScale)
            scaleX = scale
            scaleY = scale

            val dp2px = dpToPx((itemGap.toInt() / 2).toFloat())
            when (orientation) {
                ViewPager2.ORIENTATION_HORIZONTAL -> {
                    translationX = position * dp2px + if (position > 0) {
                        (-width * (1f - scale))
                    } else {
                        (width * (1f - scale))
                    }
                }

                ViewPager2.ORIENTATION_VERTICAL -> {
                    translationY = position * dp2px + if (position > 0) {
                        (-width * (1f - scale))
                    } else {
                        (width * (1f - scale))
                    }
                }

                else -> {
                    throw IllegalArgumentException("Gives correct orientation value,")
                }
            }

            if (unSelectedItemAlpha != 1f) {
                alpha = when {
                    position >= -1f && position <= 1f -> {
                        0.5f + ((1 - abs(position)) * 0.5f)
                    }

                    else -> {
                        0.5f / abs(position * position)
                    }
                }
            }
        }
    }


}
