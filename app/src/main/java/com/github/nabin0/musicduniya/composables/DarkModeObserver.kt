package com.github.nabin0.musicduniya.composables

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun DarkModeObserver(
    onDarkModeChanged: (Boolean) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val isDarkMode =
        configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    DisposableEffect(configuration) {
        onDispose {
        }
    }

    LaunchedEffect(isDarkMode) {
        onDarkModeChanged(isDarkMode)
    }
}