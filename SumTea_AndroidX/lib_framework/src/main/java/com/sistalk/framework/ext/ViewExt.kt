package com.sistalk.framework.ext

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
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