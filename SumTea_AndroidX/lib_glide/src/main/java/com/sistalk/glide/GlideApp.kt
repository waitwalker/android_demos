package com.sistalk.glide

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sistalk.framework.manager.ActivityManager
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
    if (ActivityManager.isActivityDestroy(context))return
    val options = RequestOptions.circleCropTransform()
    Glide.with(context).load(url)
        .placeholder(R.mipmap.default_img)
        .error(R.mipmap.default_img)
        .skipMemoryCache(false)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .apply(options)
        .into(this)
}

fun ImageView.setUrlCircleBorder(url: String?,borderWidth:Float,borderColor:Int) {
    if (ActivityManager.isActivityDestroy(context))return
    Glide.with(context).load(url)
        .placeholder(R.mipmap.default_img)
        .error(R.mipmap.default_img)
        .skipMemoryCache(false)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .transform(CircleBorderTransform(borderWidth,borderColor))
        .into(this)
}