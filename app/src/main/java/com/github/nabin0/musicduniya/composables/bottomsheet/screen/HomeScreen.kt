package com.github.nabin0.musicduniya.composables.bottomsheet.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.nabin0.musicduniya.composables.AudioMiniPlayer
import com.github.nabin0.musicduniya.composables.DarkModeObserver
import com.github.nabin0.musicduniya.composables.bottomsheet.SheetContent
import com.github.nabin0.musicduniya.composables.bottomsheet.collapsed.SheetCollapsed
import com.github.nabin0.musicduniya.composables.bottomsheet.expanded.SheetExpanded
import com.github.nabin0.musicduniya.extension.currentFraction
import com.github.nabin0.musicduniya.navigation.MainNavigationGraph
import com.github.nabin0.musicduniya.screens.BottomScreens
import com.github.nabin0.musicduniya.screens.FullPlayerScreen
import com.github.nabin0.musicduniya.viewmodels.AudioPlayerProvider
import com.github.nabin0.musicduniya.viewmodels.MusicPlayerSharedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(musicPlayerSharedViewModel: MusicPlayerSharedViewModel) {

    val navHostController = rememberNavController()

    val screensList = listOf(BottomScreens.Songs, BottomScreens.Playlists)

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screensList.any {
        it.route == currentDestination?.route
    }

    val isDarkTheme = isSystemInDarkTheme()
    var isDarkMode by remember { mutableStateOf(isDarkTheme) }
    DarkModeObserver { updatedDarkMode ->
        if (isDarkMode != updatedDarkMode) {
            isDarkMode = updatedDarkMode
        }
    }

    val currentSelectedAudio = musicPlayerSharedViewModel.currentSelectedAudio
    val showBottomBar by AudioPlayerProvider.showMiniPlayer.collectAsState()

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    var peekHeight by remember {
        mutableStateOf(300.dp)
    }

    var sheetGesturesEnabled by remember {
        mutableStateOf(false)
    }

    val sheetToggle: () -> Unit = {
        scope.launch {
            if (scaffoldState.bottomSheetState.isCollapsed) {
                scaffoldState.bottomSheetState.expand()
            } else {
                scaffoldState.bottomSheetState.collapse()
            }
        }
    }

    val density = LocalDensity.current

    val context = LocalContext.current as Activity

    BackHandler(scaffoldState.bottomSheetState.isExpanded) {
        if (scaffoldState.bottomSheetState.isExpanded) {
            scope.launch {
                scaffoldState.bottomSheetState.collapse()
            }
        } else {
            context.finish()
        }
    }

    BottomSheetScaffold(
        sheetGesturesEnabled = sheetGesturesEnabled,
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetContent = {
            SheetContent {
                SheetExpanded {
                    FullPlayerScreen(audioId = currentSelectedAudio)
                }
                SheetCollapsed(
                    isCollapsed = scaffoldState.bottomSheetState.isCollapsed,
                    currentFraction = scaffoldState.currentFraction,
                    onSheetClick = sheetToggle,
                    modifier = Modifier.onGloballyPositioned {
                        peekHeight = with(density) { it.size.height.toDp() }
                    }
                ) {
                    if (bottomBarDestination) {
                        Column {
                            if (showBottomBar) {
                                sheetGesturesEnabled = true
                                if (isDarkMode)
                                    AudioMiniPlayer()
                                else AudioMiniPlayer()
                            }
                            BottomBar(
                                navHostController = navHostController,
                                currentDestination = currentDestination,
                                screens = screensList
                            )
                        }
                    }
                }
            }

        },
        sheetPeekHeight = peekHeight
    ) {
        MainNavigationGraph(
            navHostController = navHostController,
            paddingValues = it
        )
    }
}


@Composable
fun BottomBar(
    navHostController: NavHostController,
    currentDestination: NavDestination?,
    screens: List<BottomScreens>,
) {

    NavigationBar {
        screens.forEach { screen ->
            AddBottomNavItem(
                navHostController = navHostController,
                screen = screen,
                currentDestination = currentDestination
            )
        }
    }

}

@Composable
fun RowScope.AddBottomNavItem(
    navHostController: NavHostController,
    screen: BottomScreens,
    currentDestination: NavDestination?,
) {
    NavigationBarItem(selected = currentDestination?.route == screen.route, onClick = {
        if (currentDestination?.route != screen.route) {
            navHostController.navigate(screen.route) {
                popUpTo(BottomScreens.Songs.route)
                launchSingleTop = true
            }
        }
    }, icon = {
        androidx.compose.material3.Icon(
            imageVector = screen.icon,
            contentDescription = "Icon"
        )
    }, label = {
        androidx.compose.material3.Text(text = screen.title)
    })
}
