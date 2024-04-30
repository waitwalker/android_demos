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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.supercharge.shimmerlayout.ShimmerLayout


class GalleryAdapter: ListAdapter<PhotoItem, MyViewHolder>(DIFFCALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // 加载xml 布局资源
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell,parent,false)
        val holder = MyViewHolder(view)

        holder.itemView.setOnClickListener(View.OnClickListener {
            val photoItem = getItem(holder.adapterPosition)
            Bundle().apply {
                putString("fullURL",photoItem.fullURL)
                putInt("photoId",photoItem.photoId)
                putString("previewURL",photoItem.previewURL)
                val list = ArrayList<String>()
                currentList.map {
                    list.add(it.fullURL)
                }
                putStringArrayList("photos",list)
                putInt("currentIndex",holder.adapterPosition)
//                holder.itemView.findNavController().navigate(R.id.action_galleryFragment_to_photoFragment, this)
                holder.itemView.findNavController().navigate(R.id.action_galleryFragment_to_pagerPhotoFragment, this)
            }
        })
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.findViewById<ShimmerLayout>(R.id.shimmerLayoutCell).apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }
        holder.itemView.findViewById<ImageView>(R.id.imageView).layoutParams.height = getItem(position).photoHeight
        holder.itemView.findViewById<TextView>(R.id.textViewUser).text = getItem(position).photoUser
        Glide.with(holder.itemView)
            .load(getItem(position).previewURL)
            .placeholder(R.drawable.photo_placeholder)
            .listener(object : RequestListener<Drawable>{
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
                        holder.itemView.findViewById<ShimmerLayout>(R.id.shimmerLayoutCell)?.stopShimmerAnimation()
                    }
                }
            })
            .into(holder.itemView.findViewById(R.id.imageView))
    }
    object DIFFCALLBACK: DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId
        }
    }

}

class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

}