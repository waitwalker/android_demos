package com.sistalk.networkdemo

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.LruCache
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.textView)
        val queue = Volley.newRequestQueue(this)
        val url = "https://www.jd.com";
        val stringRequest = StringRequest(Request.Method.GET, url, { data ->
            textView.text = data
        }, { error ->
            Log.e("main", "onErrorResponse:$error")
        })

        queue.add(stringRequest)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val imageUrl = "https://cdn.pixabay.com/photo/2024/04/20/14/35/squirrel-8708701_150.jpg";
        // 加载图片
        val imageLoader = ImageLoader(queue, object : ImageLoader.ImageCache {
            // 添加缓存
            val cache: LruCache<String, Bitmap> = LruCache(50)
            override fun getBitmap(imgUrl: String?): Bitmap? {
                return cache.get(imgUrl)
            }

            override fun putBitmap(currentUrl: String?, bitMap: Bitmap?) {
                cache.put(currentUrl, bitMap)
            }
        })
        imageLoader.get(imageUrl, object : ImageLoader.ImageListener {
            override fun onErrorResponse(p0: VolleyError?) {

            }

            override fun onResponse(p0: ImageLoader.ImageContainer?, p1: Boolean) {
                if (p0 != null) {
                    imageView.setImageBitmap(p0.bitmap)
                }
            }
        })
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipeRefreshLayout.setOnRefreshListener {
            // glide使用
            val imageView2 = findViewById<ImageView>(R.id.imageView2)
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .listener(@SuppressLint("CheckResult") object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (swipeRefreshLayout.isRefreshing) {
                            swipeRefreshLayout.isRefreshing = false
                        }
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (swipeRefreshLayout.isRefreshing) {
                            swipeRefreshLayout.isRefreshing = false
                        }
                        return false
                    }
                })
                .into(imageView2)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}