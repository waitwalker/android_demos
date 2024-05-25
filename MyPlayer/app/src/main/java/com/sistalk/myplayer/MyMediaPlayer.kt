package com.sistalk.myplayer

import android.media.MediaPlayer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

class MyMediaPlayer:MediaPlayer(),DefaultLifecycleObserver {
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        pause()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        playerResume()
    }

    fun playerResume() {
        start()
    }

}