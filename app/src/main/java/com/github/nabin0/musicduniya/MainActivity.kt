package com.github.nabin0.musicduniya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.nabin0.musicduniya.presentation.navigation.MainNavigationGraph
import com.github.nabin0.musicduniya.presentation.screens.BottomScreens
import com.github.nabin0.musicduniya.presentation.ui.theme.MusicDuniyaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicDuniyaTheme {

                val navHostController = rememberNavController()

                val screensList = listOf(BottomScreens.Songs, BottomScreens.Playlists)

                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val bottomBarDestination = screensList.any {
                    it.route == currentDestination?.route
                }

                Scaffold(
                    bottomBar = {
                        if (bottomBarDestination) {
                            BottomBar(
                                navHostController = navHostController,
                                currentDestination = currentDestination,
                                screens = screensList
                            )
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val navHostController = rememberNavController()

    val screensList = listOf(BottomScreens.Songs, BottomScreens.Playlists)

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screensList.any {
        it.route == currentDestination?.route
    }
    MusicDuniyaTheme {
        BottomBar(currentDestination = currentDestination, screens = screensList, navHostController = navHostController)
    }
}