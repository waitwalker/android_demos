package com.sistalk.gallery

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.supercharge.shimmerlayout.ShimmerLayout


class GalleryAdapter : ListAdapter<PhotoItem, MyViewHolder>(DIFFCALLBACK) {
    // 创建属于类的常量
    companion object {
        class Room {
            fun studentCount(): Int {
                return 30
            }
        }

        const val NORMAL_VIEW_TYPE = 0
        const val FOOTER_VIEW_TYPE = 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val holder: MyViewHolder
        if (viewType == NORMAL_VIEW_TYPE) {
            // 加载xml 布局资源
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell, parent, false)
            holder = MyViewHolder(view)

            holder.itemView.setOnClickListener(View.OnClickListener {
                val photoItem = getItem(holder.adapterPosition)
                Bundle().apply {
                    putString("fullURL", photoItem.fullURL)
                    putInt("photoId", photoItem.photoId)
                    putString("previewURL", photoItem.previewURL)
                    val list = ArrayList<String>()
                    currentList.map {
                        list.add(it.fullURL)
                    }
                    putStringArrayList("photos", list)
                    putInt("currentIndex", holder.adapterPosition)
//                holder.itemView.findNavController().navigate(R.id.action_galleryFragment_to_photoFragment, this)
                    holder.itemView.findNavController()
                        .navigate(R.id.action_galleryFragment_to_pagerPhotoFragment, this)
                }
            })
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.gallery_footer, parent, false).also {
                    (it.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                }
            holder = MyViewHolder(view)
        }
        return holder
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) return FOOTER_VIEW_TYPE else NORMAL_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == itemCount - 1) {
            return
        }
        holder.itemView.findViewById<ShimmerLayout>(R.id.shimmerLayoutCell).apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }
        holder.itemView.findViewById<ImageView>(R.id.imageView).layoutParams.height =
            getItem(position).photoHeight
        holder.itemView.findViewById<TextView>(R.id.textViewUser).text = getItem(position).photoUser
        Glide.with(holder.itemView)
            .load(getItem(position).previewURL)
            .placeholder(R.drawable.photo_placeholder)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also {
                        holder.itemView.findViewById<ShimmerLayout>(R.id.shimmerLayoutCell)
                            ?.stopShimmerAnimation()
                    }
                }
            })
            .into(holder.itemView.findViewById(R.id.imageView))
    }

    object DIFFCALLBACK : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem == newItem
        }
    }

}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}