package com.sistalk.glide.transform

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class CircleBorderTransform(private val borderWidth:Float,private val borderColor:Int):CircleCrop() {
    private var borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    init {
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val transform = super.transform(pool, toTransform, outWidth, outHeight)
        val canvas = Canvas(transform)
        val halfWidth = (outWidth/2).toFloat()
        val halfHeight = (outHeight/2).toFloat()
        canvas.drawCircle(halfWidth,halfHeight,halfWidth.coerceAtLeast(halfHeight) - borderWidth/2,borderPaint)
        canvas.setBitmap(null)
        return transform
    }
}