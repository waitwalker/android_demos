package com.sistalk.framework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewParent
import android.widget.LinearLayout
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * adapter 基类
 * 提供创建ViewHolder能力，提供添加头尾布局能力
 * */
abstract class BaseRecycleViewAdapter<T, B : ViewBinding> : RecyclerView.Adapter<BaseViewHolder>() {
    /// 数据列表
    private var data: MutableList<T> = mutableListOf()

    private lateinit var mHeaderLayout: LinearLayout
    private lateinit var mFooterLayout: LinearLayout

    companion object {
        const val HEADER_VIEW = 0x10000111
        const val FOOTER_VIEW = 0x10000222
    }

    /// Item 点击监听
    var onItemClickListener: ((view: View, position: Int) -> Unit)? = null

    /// 长按监听
    private var onItemLongClickListener: ((view: View, position: Int) -> Boolean) = {_,_ ->false}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val baseViewHolder: BaseViewHolder
        when (viewType) {
            HEADER_VIEW -> {
                val headerParent: ViewParent? = mHeaderLayout.parent
                if (headerParent is ViewGroup) {
                    headerParent.removeView(mHeaderLayout)
                }
                baseViewHolder = BaseViewHolder(mHeaderLayout)
            }

            FOOTER_VIEW -> {
                val footerParent: ViewParent? = mFooterLayout.parent
                if (footerParent is ViewGroup) {
                    footerParent.removeView(mFooterLayout)
                }
                baseViewHolder = BaseViewHolder(mFooterLayout)
            }

            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                baseViewHolder = onCreateDefViewHolder(layoutInflater, parent, viewType)
                bindViewClickListener(baseViewHolder)
            }
        }
        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder.itemViewType) {
            HEADER_VIEW, FOOTER_VIEW-> {
                return
            } else-> {
                if (holder is BaseBindViewHolder<*>) {
                    holder as BaseBindViewHolder<B>
                    val realPosition = position - headerLayoutCount
                    val item = getItem(realPosition)
                    item?.let {
                        onBindDefViewHolder(holder, item, position)
                    }
                }
            }
        }
    }

    protected open fun bindViewClickListener(holder: BaseViewHolder) {
        onItemClickListener?.let {
            holder.itemView.setOnClickListener { _ ->
                var position = holder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                position -= headerLayoutCount
                it.invoke(holder.itemView, position)
            }
        }

        onItemLongClickListener.let {
            holder.itemView.setOnLongClickListener { _ ->
                var position = holder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                position -= headerLayoutCount
                it.invoke(holder.itemView, position)
            }
        }
    }

    /// 绑定数据
    protected abstract fun onBindDefViewHolder(holder: BaseBindViewHolder<B>,item:T?,position: Int)

    override fun getItemViewType(position: Int): Int {
        return if (hasHeaderView() && position == headerLayoutCount) {
            HEADER_VIEW
        } else if (hasFooterView() && position == footerViewPosition) {
            FOOTER_VIEW
        } else {
            val realPosition = if (hasHeaderView()) {
                position - 1
            } else {
                position
            }
            getDefItemViewType(realPosition)
        }
    }

    override fun getItemCount(): Int {
        return headerLayoutCount + getDefItemCount() + footerLayoutCount
    }

    protected open fun getDefItemCount():Int {
        return data.size
    }

    protected open fun getDefItemViewType(position: Int):Int {
        return super.getItemViewType(position)
    }

    protected open fun onCreateDefViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return BaseBindViewHolder(getViewBinding(layoutInflater, parent, viewType))
    }

    abstract fun getViewBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): B

    fun addHeadView(view: View,index:Int = -1):Int {
        if (!this::mHeaderLayout.isInitialized) {
            mHeaderLayout = LinearLayout(view.context)
            mHeaderLayout.orientation = LinearLayout.VERTICAL
            mHeaderLayout.layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        val childCount = mHeaderLayout.childCount
        var mIndex = index
        if (index < 0 || index > childCount) {
            mIndex = childCount
        }
        mHeaderLayout.addView(view,mIndex)
        if (mHeaderLayout.childCount == 1) {
            notifyItemInserted(headerViewPosition)
        }
        return mIndex
    }

    val headerViewPosition:Int = 0

    fun hasHeaderView(): Boolean {
        return this::mHeaderLayout.isInitialized && mHeaderLayout.childCount > 0
    }

    val headerLayoutCount: Int
        get() {
            return if (hasHeaderView()) {
                1
            } else {
                0
            }
        }

    fun hasFooterView():Boolean {
        return this::mFooterLayout.isInitialized && mFooterLayout.childCount > 0
    }

    val footerViewPosition:Int
        get() = headerLayoutCount + data.size

    val footerLayoutCount:Int
        get() {
            return if (hasFooterView()) {
                1
            } else {
                0
            }
        }

    val footerViewCount:Int
        get() {
            return if (hasFooterView()) {
                mFooterLayout.childCount
            } else {
                0
            }
        }

    fun getItem(@IntRange(from = 0) position: Int):T? {
        if (position >= data.size) return null
        return data[position]
    }
}