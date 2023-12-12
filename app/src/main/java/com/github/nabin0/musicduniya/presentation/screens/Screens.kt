package com.github.nabin0.musicduniya.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomScreens(
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    object Songs : BottomScreens(
        title = "Songs",
        route = "songs",
        icon = Icons.Rounded.MusicNote
    )
    object Playlists : BottomScreens(
        title = "Playlists",
        route = "playlists",
        icon = Icons.Rounded.PlaylistPlay
    )
}

sealed class Screens(val route: String){
    object FullPlayerScreen: Screens("full player screen")
}