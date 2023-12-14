package com.github.nabin0.musicduniya.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomScreens(
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    data object Songs : BottomScreens(
        title = "Songs",
        route = "songs",
        icon = Icons.Rounded.MusicNote
    )
    data object Playlists : BottomScreens(
        title = "Playlists",
        route = "playlists",
        icon = Icons.Rounded.PlaylistPlay
    )
}

sealed class Screens(val route: String){
    data object FullPlayerScreen: Screens("full player screen/{AUDIO_ID}")
}