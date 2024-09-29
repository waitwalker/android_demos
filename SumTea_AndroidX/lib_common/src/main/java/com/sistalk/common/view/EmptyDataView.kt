package com.sistalk.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.sistalk.common.R
import com.sistalk.common.databinding.ViewEmptyDataBinding

/// 占位页面
class EmptyDataView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mBinding: ViewEmptyDataBinding? = null

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mBinding = ViewEmptyDataBinding.inflate(LayoutInflater.from(context), this, true)
        obtainAttributes(context, attrs)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.EmptyDataView)
            val emptyText = ta.getString(R.styleable.EmptyDataView_emptyText)
            val emptyImage =
                ta.getResourceId(R.styleable.EmptyDataView_emptyImage, R.mipmap.ic_data_empty)
            val bgColor = ta.getColor(
                R.styleable.EmptyDataView_bg_color,
                ContextCompat.getColor(context, R.color.white)
            )
            val emptyPaddingBottom =
                ta.getDimension(R.styleable.EmptyDataView_emptyPaddingBottom, 0.0f).toInt()
            ta.recycle()
            if (!emptyText.isNullOrEmpty()) {
                mBinding?.tvNoData?.text = emptyText
            }
            mBinding?.ivNoData?.setImageResource(emptyImage)
            setBackgroundColor(bgColor)
            setPadding(0, 0, 0, emptyPaddingBottom)
        }
    }

    fun setBgColor(color: Int): EmptyDataView {
        setBackgroundColor(color)
        return this
    }

    fun setImageResource(resId: Int): EmptyDataView {
        if (resId == 0) {
            mBinding?.tvNoData?.visibility = GONE
            return this
        }
        mBinding?.ivNoData?.visibility = VISIBLE
        mBinding?.ivNoData?.setImageResource(resId)
        return this
    }

    fun setText(string: String?): EmptyDataView {
        mBinding?.tvNoData?.text = string
        return this
    }
}