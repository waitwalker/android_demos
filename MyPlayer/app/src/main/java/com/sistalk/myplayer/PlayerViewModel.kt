package com.sistalk.myplayer

import android.media.MediaPlayer
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class PlayerStatus {
    Playing,
    Paused,
    Completed,
    NotReady
}

class PlayerViewModel:ViewModel() {
    private val _playerStatus = MutableLiveData(PlayerStatus.NotReady)
    val playerStatus:LiveData<PlayerStatus> = _playerStatus


    private var controllerDisplayShowTime = 0L
    val mediaPlayer = MyMediaPlayer()
    private val _controllerFrameVisibility = MutableLiveData(View.INVISIBLE)
    val controllerFrameVisibility:LiveData<Int> = _controllerFrameVisibility

    private val _progressBarVisibility = MutableLiveData(View.VISIBLE)
    val progressBarVisibility:LiveData<Int> = _progressBarVisibility
    private val _videoResolution = MutableLiveData(Pair(0,0))
    val videoResolution:LiveData<Pair<Int,Int>> = _videoResolution

    private val _bufferPercent = MutableLiveData(0)
    val bufferPercent:LiveData<Int> = _bufferPercent

    init {
        loadVideo()
    }

    private fun loadVideo() {
        mediaPlayer.apply {
            _progressBarVisibility.value = View.VISIBLE
            _playerStatus.value = PlayerStatus.NotReady
            // 设置源
            setDataSource("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4\n")
            // 准备监听
            setOnPreparedListener{
                _progressBarVisibility.value = View.INVISIBLE
                //isLooping = true
                it.start()
                _playerStatus.value = PlayerStatus.Playing
            }

            setOnVideoSizeChangedListener { _, width, height ->
                _videoResolution.value = Pair(width,height)
            }

            setOnBufferingUpdateListener { mp, percent ->
                _bufferPercent.value = percent
            }

            setOnSeekCompleteListener {
                mediaPlayer.start()
                _progressBarVisibility.value = View.INVISIBLE
            }

            setOnSeekCompleteListener {
                _playerStatus.value = PlayerStatus.Completed
            }

            prepareAsync()
        }
    }

    fun playerSeekToProgress(progress:Int) {
        _progressBarVisibility.value = View.VISIBLE
        mediaPlayer.seekTo(progress)
    }

    fun toggleControllerVisibility() {
        if (_controllerFrameVisibility.value == View.INVISIBLE) {
            _controllerFrameVisibility.value = View.VISIBLE
            controllerDisplayShowTime = System.currentTimeMillis()
            viewModelScope.launch {
                delay(3000)
                if (System.currentTimeMillis() - controllerDisplayShowTime > 3000) {
                    _controllerFrameVisibility.value = View.INVISIBLE
                }
            }
        } else {
            _controllerFrameVisibility.value = View.INVISIBLE
        }
    }

    fun togglePlayerStatus() {
        when(_playerStatus.value) {
            PlayerStatus.Playing -> {
                mediaPlayer.pause()
                _playerStatus.value = PlayerStatus.Paused
            }
            PlayerStatus.Paused -> {
                mediaPlayer.start()
            _playerStatus.value = PlayerStatus.Playing
            }
            PlayerStatus.Completed->{
                mediaPlayer.start()
                _playerStatus.value = PlayerStatus.Playing
            } else -> {
                return
            }
        }
    }

    fun emmitVideoResolution() {
        _videoResolution.value = _videoResolution.value;
    }

    // 释放资源
    override fun onCleared() {
        super.onCleared()
        // 释放占用的软硬件资源主要硬件资源
        mediaPlayer.release()
    }
}