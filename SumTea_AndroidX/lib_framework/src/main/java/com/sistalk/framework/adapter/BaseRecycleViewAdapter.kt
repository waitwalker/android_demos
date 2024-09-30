package com.sistalk.framework.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * adapter 基类
 * 提供创建ViewHolder能力，提供添加头尾布局能力
 * */
abstract class BaseRecycleViewAdapter<T,B: ViewBinding>:RecyclerView.Adapter<BaseViewHolder>() {
    /// 数据列表
    private var data:MutableList<T> = mutableListOf()
}