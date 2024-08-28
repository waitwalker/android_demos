package com.sistalk.banner.drawer

import android.graphics.Canvas
import com.sistalk.banner.base.BaseDrawer

interface IDrawer {

    fun onLayout(changed:Boolean,left:Int,top:Int,right:Int,bottom:Int)

    fun onMeasure(widthMeasureSpec:Int, heightMeasureSpec:Int):BaseDrawer.MeasureResult

    fun onDraw(canvas:Canvas)
}