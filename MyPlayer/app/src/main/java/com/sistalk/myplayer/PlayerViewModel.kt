package com.sistalk.myplayer

import android.media.MediaPlayer
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayerViewModel:ViewModel() {
    private val mediaPlayer = MediaPlayer()

    private val _progressBarVisibility = MutableLiveData(View.VISIBLE)
    val progressBarVisibility:LiveData<Int> = _progressBarVisibility
    private val _videoResolution = MutableLiveData(Pair(0,0))
    val videoResolution:LiveData<Pair<Int,Int>> = _videoResolution

    fun loadVideo() {
        mediaPlayer.apply {
            _progressBarVisibility.value = View.VISIBLE
            setDataSource("https://www.youtube.com/watch?v=rDvxlZcpcI4")
            setOnPreparedListener{
                _progressBarVisibility.value = View.INVISIBLE
                isLooping = true
                it.start()
            }

            setOnVideoSizeChangedListener { _, width, height ->
                _videoResolution.value = Pair(width,height)
            }
            prepareAsync()
        }
    }

    // 释放资源
    override fun onCleared() {
        super.onCleared()
        // 释放占用的软硬件资源主要硬件资源
        mediaPlayer.release()
    }
}