package com.sistalk.glide.transform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.renderscript.RSRuntimeException
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.sistalk.glide.blur.FastBlur
import com.sistalk.glide.blur.RSBlur
import java.security.MessageDigest

class BlurTransform @JvmOverloads constructor(
    private val radius:Int = MAX_RADIUS,
    private val sampling:Int = DEFAULT_DOWN_SAMPLING
    ):BitmapTransform() {



    companion object {
        private const val VERSION = 1
        private const val ID = "com.sum.glide.transform.BlurTransform.$VERSION"
        private const val MAX_RADIUS = 25
        private const val DEFAULT_DOWN_SAMPLING = 1
    }

    override fun equals(other: Any?): Boolean {
        TODO("Not yet implemented")
    }

    override fun hashCode(): Int {
        TODO("Not yet implemented")
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        TODO("Not yet implemented")
    }


    override fun transform(
        context: Context,
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val scaleWidth = width / sampling
        val scaleHeight = height/ sampling
        var bitmap = pool[scaleWidth,scaleHeight,Bitmap.Config.ARGB_8888]
        setCanvasBitmapDensity(toTransform,bitmap)
        val canvas = Canvas(bitmap)
        canvas.scale(1/sampling.toFloat(),1/sampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(toTransform,0f,0f,paint)
        bitmap = try {
            RSBlur.blur(context,bitmap,radius)
        } catch (e:RSRuntimeException) {
            FastBlur.blur(bitmap,radius,true)
        }
        return bitmap
    }
}