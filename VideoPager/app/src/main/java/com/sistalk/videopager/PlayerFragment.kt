package com.sistalk.videopager

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.sistalk.videopager.databinding.FragmentPlayerBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerFragment(private val url:String) : Fragment() {
    private val mediaPlayer = MediaPlayer()
    lateinit var binding: FragmentPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater)
        return binding.root
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer.apply {
            setOnPreparedListener {
                binding.progressBarH.max = mediaPlayer.duration
                // 有预加载后不能直接播放，因为会出现好几个连续播放
                //it.start()
                seekTo(1)
                binding.progressBar.visibility = View.INVISIBLE
            }
            setDataSource(url)
            prepareAsync()
            binding.progressBar.visibility = View.VISIBLE
        }

        // 协程作用域
        lifecycleScope.launch {
            while (true) {
                binding.progressBarH.progress = mediaPlayer.currentPosition
                delay(500)
            }
        }
        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder) {

            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                mediaPlayer.setDisplay(holder)
                mediaPlayer.setScreenOnWhilePlaying(true)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
        lifecycleScope.launch {
            // 守护进程
            while (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                delay(500)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }
}