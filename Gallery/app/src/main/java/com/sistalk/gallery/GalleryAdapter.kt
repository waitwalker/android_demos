package com.sistalk.gallery

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.supercharge.shimmerlayout.ShimmerLayout


class GalleryAdapter(val viewModel: GalleryViewModel) :
    PagedListAdapter<PhotoItem, PhotoViewHolder>(DIFFCALLBACK) {
    var networkStatus: NetworkStatus? = null
    var footViewStatus = DATA_STATUS_CAN_LOAD_MORE
    private var hasFooter = false

    fun updateNetworkStatus(networkStatus: NetworkStatus?) {
        this.networkStatus = networkStatus
    }

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        when (viewType) {
            R.layout.gallery_cell-> {
                return PhotoViewHolder.newInstance(parent).also {holder->
                    holder.itemView.setOnClickListener {

                    }

                }
            }
        }
        val holder: PhotoViewHolder
        if (viewType == NORMAL_VIEW_TYPE) {
            // 加载xml 布局资源
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell, parent, false)
            holder = PhotoViewHolder(view)

            holder.itemView.setOnClickListener(View.OnClickListener {
                val photoItem = getItem(holder.adapterPosition)
                Bundle().apply {
                    if (photoItem != null) {
                        putString("fullURL", photoItem.fullURL)
                    }
                    if (photoItem != null) {
                        putInt("photoId", photoItem.photoId)
                    }
                    if (photoItem != null) {
                        putString("previewURL", photoItem.previewURL)
                    }
                    val list = ArrayList<String>()
                    currentList?.map {
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
                LayoutInflater.from(parent.context).inflate(R.layout.gallery_footer, parent, false)
                    .also {
                        (it.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                            true
                        it.setOnClickListener { itemView ->
                            itemView.findViewById<ProgressBar>(R.id.progressBar).visibility =
                                View.VISIBLE
                            itemView.findViewById<TextView>(R.id.textView).text = "正在加载"
//                        viewModel.fetchData()
                            viewModel.resetQuery()
                        }
                    }

            holder = PhotoViewHolder(view)
        }
        return holder
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasFooter && position == itemCount - 1) return FOOTER_VIEW_TYPE else NORMAL_VIEW_TYPE
//        return if (hasFooter && position == itemCount - 1) return R.layout.gallery_footer else R.layout.fragment_gallery
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter) 1 else 0
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        if (position == itemCount - 1) {
            with(holder.itemView) {
                when (footViewStatus) {
                    DATA_STATUS_CAN_LOAD_MORE -> {
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
                        findViewById<TextView>(R.id.textView).text = "正在加载"
                        isClickable = false
                    }

                    DATA_STATUS_NO_MORE -> {
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                        findViewById<TextView>(R.id.textView).text = "加载完毕"
                        isClickable = false
                    }

                    DATA_STATUS_NETWORK_ERROR -> {
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                        findViewById<TextView>(R.id.textView).text = "网络故障，点击重试"
                        isClickable = true
                    }
                }
            }
            return
        }
        holder.itemView.findViewById<ShimmerLayout>(R.id.shimmerLayoutCell).apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }
        holder.itemView.findViewById<ImageView>(R.id.imageView).layoutParams.height =
            getItem(position)!!.photoHeight
        holder.itemView.findViewById<TextView>(R.id.textViewUser).text =
            getItem(position)!!.photoUser
        Glide.with(holder.itemView)
            .load(getItem(position)!!.previewURL)
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

class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun newInstance(parent: ViewGroup): PhotoViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell, parent, false)
            return PhotoViewHolder(view)
        }
    }

    fun bindWithPhotoItem(photoItem: PhotoItem) {
        itemView.findViewById<ShimmerLayout>(R.id.shimmerLayoutCell).apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }
        itemView.findViewById<ImageView>(R.id.imageView).layoutParams.height =
            photoItem.photoHeight
        itemView.findViewById<TextView>(R.id.textViewUser).text = photoItem.photoUser
        Glide.with(itemView)
            .load(photoItem.previewURL)
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
                        itemView.findViewById<ShimmerLayout>(R.id.shimmerLayoutCell)
                            ?.stopShimmerAnimation()
                    }
                }
            })
            .into(itemView.findViewById(R.id.imageView))
    }
}

class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun newInstance(parent: ViewGroup): FooterViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.gallery_footer, parent, false)
            return FooterViewHolder(view)
        }
    }

    fun bindWithNetworkStatus(networkStatus: NetworkStatus?) {
        with(itemView) {
            when (networkStatus) {
                NetworkStatus.FAILED -> {
                    itemView.findViewById<TextView>(R.id.textView).text = "点击重试"
                    itemView.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                    isClickable = true
                }

                NetworkStatus.COMPLETED -> {
                    itemView.findViewById<TextView>(R.id.textView).text = "加载完毕"
                    itemView.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                    isClickable = false
                }
                 else -> {
                     itemView.findViewById<TextView>(R.id.textView).text = "正在加载"
                     itemView.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
                     isClickable = false
                 }
            }
        }
    }
}