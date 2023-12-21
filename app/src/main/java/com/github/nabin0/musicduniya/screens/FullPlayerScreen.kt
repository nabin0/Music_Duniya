package com.github.nabin0.musicduniya.screens

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.github.nabin0.audioplayer.view.AudioPlayer
import com.github.nabin0.musicduniya.R
import com.github.nabin0.musicduniya.composables.DarkModeObserver

@Composable
fun FullPlayerScreen(
    audioId: Int,
) {
    val isDarkTheme = isSystemInDarkTheme()
    var isDarkMode by remember { mutableStateOf(isDarkTheme) }
    DarkModeObserver { updatedDarkMode ->
        if (isDarkMode != updatedDarkMode) {
            isDarkMode = updatedDarkMode
        }
    }

    if (isDarkMode) FullScreenPlayerView(audioId = audioId) else FullScreenPlayerView(audioId = audioId)
}

@Composable
fun FullScreenPlayerView(audioId: Int){
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
                audioPlayer?.setCustomLayout(R.layout.audio_fullscreen_player_layout)
            }
        )
    }

}
