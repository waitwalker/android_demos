package com.sistalk.myplayer

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.View
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sistalk.myplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var playerViewModel: PlayerViewModel? = null
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_main)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        playerViewModel = mainBinding.data
//        mainBinding.lifecycleOwner = this
        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java].apply {

            progressBarVisibility.observe(this@MainActivity, Observer {
                mainBinding.progressBar.visibility = it
            })

            videoResolution.observe(this@MainActivity, Observer {
                mainBinding.playerFrame.post {
                    resizePlayer(it.first, it.second)
                }
            })
        }
        lifecycle.addObserver(playerViewModel!!.mediaPlayer)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /// 通过回调监听surfaceView
        mainBinding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {

            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                playerViewModel?.mediaPlayer?.setDisplay(holder)
                // 点亮屏幕 息屏
                playerViewModel?.mediaPlayer?.setScreenOnWhilePlaying(true)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {

            }

        })
    }

    private fun resizePlayer(width: Int, height: Int) {
        if (width == 0 || height == 0) return
        mainBinding.surfaceView.layoutParams = FrameLayout.LayoutParams(
            mainBinding.playerFrame.height * width / height,
            FrameLayout.LayoutParams.MATCH_PARENT,
            Gravity.CENTER,
        )
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("main", "切换到横屏")
            hideSystemUI()
            playerViewModel?.emmitVideoResolution()
        }
    }

    fun hideSystemUI() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

}