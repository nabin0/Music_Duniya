package com.github.nabin0.musicduniya.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.nabin0.audioplayer.view.AudioMiniPlayer
import com.github.nabin0.musicduniya.R

@Composable
fun AudioMiniPlayer(modifier: Modifier = Modifier) {

    var audioMiniPlayer: AudioMiniPlayer? = null
    Surface(modifier = modifier
        .fillMaxWidth(),
        shadowElevation = 3.dp,
        tonalElevation = 3.dp) {
        DisposableEffect(Unit) {
            onDispose {
                audioMiniPlayer?.removeCallbacks()
            }
        }
        AndroidView(
            modifier = Modifier
                .fillMaxWidth(),
            factory = { context ->
                AudioMiniPlayer(context).apply {
                    audioMiniPlayer = this
                }
            },
            update = {
                 audioMiniPlayer?.setCustomLayout(R.layout.audio_mini_player_full_screen_layout)
            }
        )
    }
}