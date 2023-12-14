package com.github.nabin0.musicduniya

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.nabin0.musicduniya.composables.AudioMiniPlayer
import com.github.nabin0.musicduniya.navigation.MainNavigationGraph
import com.github.nabin0.musicduniya.screens.BottomScreens
import com.github.nabin0.musicduniya.ui.theme.MusicDuniyaTheme
import com.github.nabin0.musicduniya.utils.ContentResolverHelper
import com.github.nabin0.musicduniya.viewmodels.AudioPlayerProvider
import dagger.hilt.android.AndroidEntryPoint

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
                val navHostController = rememberNavController()

                val screensList = listOf(BottomScreens.Songs, BottomScreens.Playlists)

                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val bottomBarDestination = screensList.any {
                    it.route == currentDestination?.route
                }

                val showBottomBar by AudioPlayerProvider.showMiniPlayer.collectAsState()
                Column(Modifier.fillMaxSize()) {
                    Scaffold(
                        bottomBar = {
                            if (bottomBarDestination) {
                                Column {
                                    if (showBottomBar) {
                                        AudioMiniPlayer()
                                    }
                                    BottomBar(
                                        navHostController = navHostController,
                                        currentDestination = currentDestination,
                                        screens = screensList
                                    )
                                }
                            }
                        }
                    ) { paddingValues ->
                        MainNavigationGraph(
                            navHostController = navHostController,
                            paddingValues = paddingValues
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        if(isFinishing){
            AudioPlayerProvider.destroy()
        }
        super.onDestroy()
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
    }, icon = { Icon(imageVector = screen.icon, contentDescription = "Icon") }, label = {
        Text(text = screen.title)
    })
}
