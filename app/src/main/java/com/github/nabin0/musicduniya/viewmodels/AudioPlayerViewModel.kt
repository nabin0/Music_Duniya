package com.github.nabin0.musicduniya.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import com.github.nabin0.audioplayer.models.Audio
import com.github.nabin0.audioplayer.view.AudioPlayer
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("StaticFieldLeak")
object AudioPlayerProvider {

    private var audioPlayer: AudioPlayer? = null
    var showMiniPlayer = MutableStateFlow(false)


    fun setPlaylist(audioList: List<Audio>) {
        audioPlayer?.setPlaylist(audioList)
    }


    fun getAudioPlayer(context: Context): AudioPlayer {
        audioPlayer = audioPlayer ?: AudioPlayer(context = context).apply {
            initializePlayer()
            removeCallbacks()
        }
        return audioPlayer as AudioPlayer
    }

    val currentPlayingAudioIndex = audioPlayer?.currentPlayingMediaIndex ?: MutableStateFlow(-1)

    fun destroy() {
        audioPlayer?.destroy()
        showMiniPlayer.value = false
        audioPlayer = null
    }
}