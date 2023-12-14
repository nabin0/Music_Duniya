package com.github.nabin0.musicduniya.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.nabin0.musicduniya.screens.BottomScreens
import com.github.nabin0.musicduniya.screens.FullPlayerScreen
import com.github.nabin0.musicduniya.screens.PlaylistsScreen
import com.github.nabin0.musicduniya.screens.Screens
import com.github.nabin0.musicduniya.screens.SongsScreen
import com.github.nabin0.musicduniya.viewmodels.AudioPlayerProvider
import com.github.nabin0.musicduniya.viewmodels.SongsListViewModel

@Composable
fun MainNavigationGraph(navHostController: NavHostController, paddingValues: PaddingValues) {
    val sharedSongsListViewModel: SongsListViewModel = hiltViewModel()

    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = paddingValues.calculateBottomPadding()
            ),
        navController = navHostController,
        route = GRAPH.MAIN_GRAPH,
        startDestination = BottomScreens.Songs.route
    ) {
        composable(route = BottomScreens.Songs.route) {

            SongsScreen(
                songsListViewModel = sharedSongsListViewModel,
                navigateToFullPlayerScreen = {
                    AudioPlayerProvider.setPlaylist(sharedSongsListViewModel.audioList)
                    navHostController.navigate(
                        Screens.FullPlayerScreen.route.replace(
                            "{AUDIO_ID}", it.toString()
                        )
                    )
                },
            )
        }

        composable(route = BottomScreens.Playlists.route) {
            PlaylistsScreen()
        }

        composable(route = Screens.FullPlayerScreen.route) { backStackEntry ->
            val audioId = backStackEntry.arguments?.getString("AUDIO_ID")
            FullPlayerScreen(
                audioId = audioId?.toInt() ?: 0,
            )
        }
    }
}

object GRAPH {
    const val MAIN_GRAPH = "main graph"
}