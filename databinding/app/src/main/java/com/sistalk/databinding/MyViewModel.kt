package com.sistalk.databinding

import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
import com.sistalk.databinding.app.MyApplication


class MyViewModel : ViewModel(), Listener {
    private lateinit var number: MutableLiveData<Int>
    private lateinit var player: ExoPlayer
    @SuppressLint("StaticFieldLeak")
    private lateinit var context: Context

    fun addContext(context: Context) {
        this.context = context
    }

    fun getNumber(): MutableLiveData<Int> {
        if (!::number.isInitialized) {
            number = MutableLiveData()
            number.value = 0
        }
        return number
    }


    fun add() {
        number.value = number.value?.plus(1);
    }


    fun preparePlayer() {
        if (!::player.isInitialized) {
            player = ExoPlayer.Builder(this.context).build()
        }
        player.addListener(this)
        val mediaItem = MediaItem.Builder()
            .setUri("https://monsterhub-editor.oss-cn-beijing.aliyuncs.com/stream/10000/10000/resource_1701180379293.mp3")
            .build()
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    fun play() {


    }

    fun pause() {

    }

    fun destroy() {}

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
        println("事件监听=${events}")
    }
}
