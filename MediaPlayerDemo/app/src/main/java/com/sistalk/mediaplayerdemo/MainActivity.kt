package com.sistalk.mediaplayerdemo

import android.media.PlaybackParams
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.sistalk.mediaplayerdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val tag:String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val videoPath = "android.resource://$packageName/${R.raw.a1}"
        mainBinding.videoView.setVideoPath(videoPath)
        mainBinding.videoView.setMediaController(MediaController(this))
        // 缓冲监听
        mainBinding.videoView.setOnPreparedListener {
            Log.d(tag,"media player id=$it")
            mainBinding.progressBar2.visibility = View.INVISIBLE
            mainBinding.progressBar.max = it.duration
            it.seekTo(3000)
            it.isLooping = true
            it.playbackParams = PlaybackParams().apply {
                speed = 2.0f //倍速
                pitch = 2.0f // 音高
            }
            it.start()
        }
        //mainBinding.videoView.start()

        // 播放完成监听
        mainBinding.videoView.setOnCompletionListener {

        }

        lifecycleScope.launch {
            while (true) {
                if (mainBinding.videoView.isPlaying) {
                    mainBinding.progressBar.progress = mainBinding.videoView.currentPosition
                }
                delay(500)
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}