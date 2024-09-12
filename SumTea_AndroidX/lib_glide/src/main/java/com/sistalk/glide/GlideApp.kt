package com.sistalk.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.ResourceCallback
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.sistalk.framework.manager.ActivityManager
import com.sistalk.framework.manager.AppManager
import com.sistalk.glide.transform.BlurTransform
import com.sistalk.glide.transform.CircleBorderTransform
import java.io.File


/*
* 加载图片，开启缓存
* */
fun ImageView.setUrl(url: String?) {
    if (ActivityManager.isActivityDestroy(context)) {
        return
    }

    Glide.with(context).load(url)
        .placeholder(R.mipmap.default_img)
        .error(R.mipmap.default_img)
        .skipMemoryCache(false)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(this)
}

fun ImageView.loadFile(file: File?) {
    if (ActivityManager.isActivityDestroy(context)) {
        return
    }

    val options = RequestOptions.circleCropTransform()
    Glide.with(context).load(file)
        .placeholder(R.mipmap.default_img)
        .error(R.mipmap.default_img)
        .skipMemoryCache(false)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .apply(options)
        .into(this)
}

fun ImageView.setUrlNoCache(url: String?) {
    if (ActivityManager.isActivityDestroy(context)) return
    Glide.with(context).load(url)
        .placeholder(R.mipmap.default_img)
        .error(R.mipmap.default_img)
        .priority(Priority.HIGH)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(this)
}

fun ImageView.setUrlCircle(url: String?) {
    if (ActivityManager.isActivityDestroy(context)) return
    val options = RequestOptions.circleCropTransform()
    Glide.with(context).load(url)
        .placeholder(R.mipmap.default_img)
        .error(R.mipmap.default_img)
        .skipMemoryCache(false)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .apply(options)
        .into(this)
}

fun ImageView.setUrlCircleBorder(url: String?, borderWidth: Float, borderColor: Int) {
    if (ActivityManager.isActivityDestroy(context)) return
    Glide.with(context).load(url)
        .placeholder(R.mipmap.default_img)
        .error(R.mipmap.default_img)
        .skipMemoryCache(false)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .transform(CircleBorderTransform(borderWidth, borderColor))
        .into(this)
}

fun ImageView.setUrlRound(url: String?, radius: Int = 10) {
    if (ActivityManager.isActivityDestroy(context)) return
    Glide.with(context).load(url)
        .placeholder(R.mipmap.default_img)
        .error(R.mipmap.default_img)
        .skipMemoryCache(false)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .transform(CenterCrop(), RoundedCorners(radius))
        .into(this)
}

fun ImageView.setUrlErrorIcon(url: String?, @DrawableRes errorRes: Int) {
    if (ActivityManager.isActivityDestroy(context)) return
    Glide.with(context).load(url)
        .placeholder(errorRes)
        .error(errorRes)
        .priority(Priority.HIGH)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(this)
}

fun ImageView.setUrlAsBitmap(url: String?, callback: (Bitmap) -> Unit) {
    if (ActivityManager.isActivityDestroy(context)) return
    Glide.with(context).asBitmap().load(url)
        .placeholder(R.mipmap.default_img)
        .error(R.mipmap.default_img)
        .into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                setImageBitmap(bitmap)
                callback(bitmap)
            }
        })
}

fun ImageView.setUrlGif(url: String?) {
    if (ActivityManager.isActivityDestroy(context)) return
    Glide.with(context).asGif().load(url)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .placeholder(R.mipmap.default_img)
        .error(R.mipmap.default_img)
        .into(this)
}

fun ImageView.setBlurView(url: String?, radius: Int = 2, sampling: Int = 1) {
    if (ActivityManager.isActivityDestroy(context)) return
    val options = RequestOptions.bitmapTransform(BlurTransform(radius, sampling))
    Glide.with(context).load(url)
        .placeholder(R.mipmap.default_img)
        .error(R.mipmap.default_img)
        .apply(options)
        .into(this)
}

fun ImageView.setScanImage(url: String?) {
    if (ActivityManager.isActivityDestroy(context)) return
    Glide.with(context).asDrawable().load(url)
        .placeholder(R.mipmap.default_img)
        .error(R.mipmap.default_img)
        .skipMemoryCache(false)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(object : CustomTarget<Drawable?>() {
            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable?>?
            ) {
                val width = resource.intrinsicWidth
                val height = resource.intrinsicHeight
                val lp = layoutParams
                lp.width = AppManager.getScreenWidthPx()
                val tempHeight = height * (lp.width.toFloat() / width)
                lp.height = tempHeight.toInt()
                layoutParams = lp
                layoutParams = lp
                setImageDrawable(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }

        })
}