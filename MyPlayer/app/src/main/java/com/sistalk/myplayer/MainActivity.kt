package com.sistalk.myplayer

import android.os.Bundle
import android.view.SurfaceHolder
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
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
//        playerViewModel = mainBinding.data
//        mainBinding.lifecycleOwner = this
        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java].apply {

            progressBarVisibility.observe(this@MainActivity, Observer {
                mainBinding.progressBar.visibility = it
            })
        }
        lifecycle.addObserver(playerViewModel!!.mediaPlayer)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /// 通过回调监听surfaceView
        mainBinding.surfaceView.holder.addCallback(object :SurfaceHolder.Callback{
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

}