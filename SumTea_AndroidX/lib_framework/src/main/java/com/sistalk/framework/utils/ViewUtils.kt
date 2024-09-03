package com.sistalk.framework.utils

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

object ViewUtils {

    // 给View设置圆角
    fun setClipViewCornerRadius(view: View?,radius:Int) {
        if (view != null) {
            if (radius > 0) {
                view.outlineProvider = object :ViewOutlineProvider(){
                    override fun getOutline(view: View?, outline: Outline?) {
                        outline?.setRoundRect(0,0,view?.width?:0,view?.height?:0,radius.toFloat())
                    }
                }
                view.clipToOutline = true
            } else {
                view.clipToOutline = false
            }
        }
    }

    // 设置view顶部圆角
    fun setClipViewCornerTopRadius(view: View?,radius:Int) {
        if (view != null) {
            if (radius > 0) {
                view.outlineProvider = object :ViewOutlineProvider(){
                    override fun getOutline(view: View?, outline: Outline?) {
                        outline?.setRoundRect(0,0,view?.width?:0,view?.height?.plus(radius)?:0,radius.toFloat())
                    }
                }
                view.clipToOutline = true
            } else {
                view.clipToOutline = false
            }
        }
    }

    // 设置view底部圆角
    fun setClipViewCornerBottomRadius(view: View?,radius:Int) {
        if (view != null) {
            if (radius > 0) {
                view.outlineProvider = object :ViewOutlineProvider(){
                    override fun getOutline(view: View?, outline: Outline?) {
                        outline?.setRoundRect(0,-radius,view?.width?:0,view?.height?:0,radius.toFloat())
                    }
                }
                view.clipToOutline = true
            } else {
                view.clipToOutline = false
            }
        }
    }


}