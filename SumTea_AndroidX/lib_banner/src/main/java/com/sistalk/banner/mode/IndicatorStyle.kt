package com.sistalk.banner.mode

interface IndicatorStyle {
    companion object {
        const val CIRCLE = 0
        const val DASH = 1 shl 1
        const val ROUND_RECT = 1 shl 2
    }
}