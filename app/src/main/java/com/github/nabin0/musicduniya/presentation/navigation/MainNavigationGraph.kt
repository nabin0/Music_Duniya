package com.github.nabin0.musicduniya.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.nabin0.musicduniya.presentation.screens.BottomScreens
import com.github.nabin0.musicduniya.presentation.screens.FullPlayerScreen
import com.github.nabin0.musicduniya.presentation.screens.PlaylistsScreen
import com.github.nabin0.musicduniya.presentation.screens.Screens
import com.github.nabin0.musicduniya.presentation.screens.SongsScreen

@Composable
fun MainNavigationGraph(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        modifier = Modifier.padding(
            bottom = paddingValues.calculateBottomPadding(),
            top = paddingValues.calculateTopPadding()
        ),
        navController = navHostController,
        route = GRAPH.MAIN_GRAPH,
        startDestination = BottomScreens.Songs.route
    ) {
        composable(route = BottomScreens.Songs.route) {
            SongsScreen(navigateToFullPlayerScreen = {
                navHostController.navigate(Screens.FullPlayerScreen.route)
            })
        }

        composable(route = BottomScreens.Playlists.route) {
            PlaylistsScreen()
        }

        composable(route = Screens.FullPlayerScreen.route){
            FullPlayerScreen()
        }
    }
}

object GRAPH {
    const val MAIN_GRAPH = "main graph"
}