package com.github.nabin0.musicduniya.screens

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.github.nabin0.audioplayer.view.AudioPlayer
import com.github.nabin0.musicduniya.R
import com.github.nabin0.musicduniya.viewmodels.AudioPlayerProvider

@Composable
fun FullPlayerScreen(
    audioId: Int,
) {
    val mContext = LocalContext.current as Activity
    var audioPlayer: AudioPlayer? = null

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer?.removeCallbacks()
        }
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = {
                AudioPlayer(mContext).apply {
                    audioPlayer = this;
                }
            },
            update = {
//                audioPlayer?.setCustomLayout(R.layout.audio_player_full_screen_layout)
                audioPlayer?.prepare()
                audioPlayer?.setAutoPlayEnabled(true)
                if (AudioPlayerProvider.currentPlayingAudioIndex.value != audioId) {
                    audioPlayer?.playMediaItemByIndex(audioId)
                }
                audioPlayer?.startForegroundService()
                AudioPlayerProvider.showMiniPlayer.value = true
            }
        )
    }

}
