package com.sistalk.banner.indicator

import android.content.Context
import android.util.AttributeSet
import com.sistalk.banner.base.BaseIndicatorView

class IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseIndicatorView(context, attrs, defStyleAttr) {

    private var mDrawerProxy: DrawerProxy
}