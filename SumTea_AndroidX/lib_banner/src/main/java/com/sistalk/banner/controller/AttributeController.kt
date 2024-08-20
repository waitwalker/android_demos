package com.sistalk.banner.controller

import android.content.Context
import android.util.AttributeSet
import com.sistalk.banner.R
import com.sistalk.banner.options.BannerOptions

/**
 * Attribute控制器
 * */
class AttributeController(var mBannerOptions: BannerOptions) {
    fun init(context: Context?,attrs:AttributeSet?) {
        if (attrs != null) {
            val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.Banner)
        }
    }
}