package com.github.nabin0.musicduniya.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SongsScreen(navigateToFullPlayerScreen:() -> Unit) {
    Text(text = "songs screen", modifier = Modifier.clickable {
        navigateToFullPlayerScreen.invoke()
    })

}