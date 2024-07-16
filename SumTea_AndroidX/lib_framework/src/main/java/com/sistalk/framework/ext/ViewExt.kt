package com.sistalk.framework.ext

import android.animation.Animator
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.icu.text.ListFormatter.Width
import android.os.SystemClock
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.sistalk.framework.R

/**
 * View扩展
 * */

/**
 * 点击事件的View扩展
 * */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    block(it as T)
}

/**
 * 长按监听
 * */
fun View.longClick(action: (view: View) -> Boolean) {
    setOnClickListener {
        action(it)
    }
}

fun View.onClick(wait: Long = 200, block: ((View) -> Unit)) {
    setOnClickListener(throttleClick(wait, block))
}

fun View.onDebounceClick(wait: Long = 200, block: ((View) -> Unit)) {
    setOnClickListener(debounceClick(wait, block))
}

fun throttleClick(wait: Long = 200, block: ((View) -> Unit)): View.OnClickListener {
    return View.OnClickListener { v ->
        val current = SystemClock.uptimeMillis()
        val lastClickTime = (v.getTag(R.id.click_time_stamp) as? Long) ?: 0
        if (current - lastClickTime > wait) {
            v.setTag(R.id.click_time_stamp, current)
            block(v)
        }
    }
}

fun debounceClick(wait: Long = 200, block: ((View) -> Unit)): View.OnClickListener {
    return View.OnClickListener { v ->
        var action = (v.getTag(R.id.click_debounce_action) as? DebounceAction)
        if (action == null) {
            action = DebounceAction(v, block)
            v.setTag(R.id.click_debounce_action, action)
        } else {
            action.block = block
        }
        v.removeCallbacks(action)
        v.postDelayed(action, wait)
    }
}

class DebounceAction(val view: View, var block: ((View) -> Unit)) : Runnable {
    override fun run() {
        if (view.isAttachedToWindow) {
            block(view)
        }
    }
}

/**
 * 设置View Margin
 * */
fun View.margin(
    leftMargin: Int = Int.MAX_VALUE,
    topMargin: Int = Int.MAX_VALUE,
    rightMargin: Int = Int.MAX_VALUE,
    bottomMargin: Int = Int.MAX_VALUE
) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    if (leftMargin != Int.MAX_VALUE) {
        params.leftMargin = leftMargin
    }

    if (topMargin != Int.MAX_VALUE) {
        params.topMargin = topMargin
    }

    if (rightMargin != Int.MAX_VALUE) {
        params.rightMargin = rightMargin
    }

    if (bottomMargin != Int.MAX_VALUE) {
        params.bottomMargin = bottomMargin
    }
}

/**
 * 设置带动画的宽度
 * */
fun View.animateWidth(
    targetValue: Int,
    duration: Long = 400,
    listener: Animator.AnimatorListener? = null,
    action: ((Float) -> Unit)? = null
) {
    post {
        ValueAnimator.ofInt(width, targetValue).apply {
            addUpdateListener {
                width(it.animatedValue as Int)
                action?.invoke(it.animatedFraction)
            }
            if (listener != null) addListener(listener)
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置高度
 * */
fun View.animateHeight(
    targetValue: Int,
    duration: Long = 400,
    listener: Animator.AnimatorListener? = null,
    action: ((Float) -> Unit)? = null
) {
    post {
        ValueAnimator.ofInt(height, targetValue).apply {
            addUpdateListener {
                height(it.animatedValue as Int)
                action?.invoke(it.animatedFraction)
            }
            if (listener != null) addListener(listener)
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置View宽度
 * */
fun View.width(width: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.width = width
    layoutParams = params
    return this
}

/**
 * 设置View 高度
 * */
fun View.height(height: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.height = height
    layoutParams = params
    return this
}

/**
 * 设置带动画的宽高
 * */
fun View.animateWidthAndHeight(
    targetWidth: Int,
    targetHeight: Int,
    duration: Long = 400,
    listener: Animator.AnimatorListener? = null,
    action: ((Float) -> Unit)? = null
) {
    post {
        val startHeight = height
        val evaluator = IntEvaluator()
        ValueAnimator.ofInt(width, targetWidth).apply {
            addUpdateListener {
                widthAndHeight(
                    it.animatedValue as Int,
                    evaluator.evaluate(it.animatedFraction, startHeight, targetHeight)
                )
                action?.invoke(it.animatedFraction)
            }
            if (listener != null) addListener(listener)
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置View宽高
 * */
fun View.widthAndHeight(width: Int, height: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.width = width
    params.height = height
    layoutParams = params
    return this
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

val View.isGone: Boolean
    get() {
        return visibility == View.VISIBLE
    }

val View.isVisible: Boolean
    get() {
        return visibility == View.VISIBLE
    }

val View.isInvisible: Boolean
    get() {
        return visibility == View.INVISIBLE
    }

/**
 * View可见性toggle 切换
 * */
fun View.toggleVisibility() {
    visibility = if (visibility == View.GONE) View.VISIBLE else View.GONE
}


/**
 * 设置高度，限制在min 和 max之间
 * */
fun View.limitHeight(h: Int, min: Int, max: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    when {
        h < min -> params.height = min
        h > max -> params.height = max
        else -> params.height = h
    }
    layoutParams = params
    return this
}

/**
 * 设置宽度，限制在min 和 max之间
 * */
fun View.limitWidth(w: Int, min: Int, max: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    when {
        w < min -> params.width = min
        w > max -> params.width = max
        else -> params.width = w
    }
    layoutParams = params
    return this
}

/**
 * 设置View的margin
 * */
fun View.margin(
    startMargin: Int = Int.MAX_VALUE,
    topMargin: Int = Int.MAX_VALUE,
    endMargin: Int = Int.MAX_VALUE,
    bottomMargin: Int = Int.MAX_VALUE,
    supportRTL: Boolean = true
): View {
    val params = layoutParams as? ViewGroup.MarginLayoutParams
    if (startMargin != Int.MAX_VALUE) {
        if (supportRTL)
            params?.marginStart = startMargin
        else
            params?.leftMargin = startMargin
    }

    if (topMargin != Int.MAX_VALUE) {
        params?.topMargin = topMargin
    }

    if (endMargin != Int.MAX_VALUE) {
        if (supportRTL)
            params?.marginEnd = endMargin
        else
            params?.rightMargin = endMargin
    }
    if (bottomMargin != Int.MAX_VALUE) {
        params?.bottomMargin = bottomMargin
    }
    params?.let {
        layoutParams = it
    }
    return this
}

/**
 * 获取View的截图，支持截取整个RecyclerView列表的长截图
 * 注意：请确保View的已经测量完毕，如果宽高为0，则会抛异常
 * */
fun View.toBitmap(): Bitmap {
    if (measuredWidth == 0 || measuredHeight == 0) {
        throw RuntimeException("make sure measure width or measure height")
    }
    return when (this) {
        is RecyclerView -> {
            this.scrollToPosition(0)
            this.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )

            val bmp = Bitmap.createBitmap(width, measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            if (background != null) {
                background.setBounds(0, 0, width, measuredHeight)
                background.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            this.draw(canvas)
            this.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST)
            )
            bmp
        }

        else -> {
            val screenshot =
                Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_4444)
            val canvas = Canvas(screenshot)
            if (background != null) {
                background.setBounds(0, 0, width, measuredHeight)
                background.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            draw(canvas)
            screenshot
        }
    }
}

val View.layoutGravity: Int
    get() = when (val lp = layoutParams) {
        is FrameLayout.LayoutParams -> lp.gravity
        is LinearLayout.LayoutParams -> lp.gravity
        else -> Gravity.NO_GRAVITY
    }

fun View.layoutGravity(gravity: Int) {
    when (val lp = layoutParams) {
        is FrameLayout.LayoutParams -> lp.gravity = gravity
        is LinearLayout.LayoutParams -> lp.gravity = gravity
        else -> return
    }
}

inline val ViewGroup.children
    get() = (0 until childCount).map {
        getChildAt(it)
    }