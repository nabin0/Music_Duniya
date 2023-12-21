package com.github.nabin0.musicduniya

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nabin0.musicduniya.composables.bottomsheet.screen.HomeScreen
import com.github.nabin0.musicduniya.ui.theme.MusicDuniyaTheme
import com.github.nabin0.musicduniya.utils.ContentResolverHelper
import com.github.nabin0.musicduniya.viewmodels.AudioPlayerProvider
import com.github.nabin0.musicduniya.viewmodels.MusicPlayerSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        AudioPlayerProvider.getAudioPlayer(this)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                ContentResolverHelper(this)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }


        setContent {
            MusicDuniyaTheme {
                HomeScreen(musicPlayerSharedViewModel = hiltViewModel())
            }
        }
    }

    override fun onDestroy() {
        if (isFinishing) {
            AudioPlayerProvider.destroy()
        }
        super.onDestroy()
    }
}
