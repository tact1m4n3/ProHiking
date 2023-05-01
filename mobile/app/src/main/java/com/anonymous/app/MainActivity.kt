package com.anonymous.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.anonymous.app.ui.theme.AppTheme
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.setPreloadingPadding
import ovh.plrapps.mapcompose.api.shouldLoopScale
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.MapUI
import ovh.plrapps.mapcompose.ui.state.MapState
import java.io.BufferedInputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme() {
                AppUI()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUI() {
    val navController = rememberNavController()
    val navBarItems = listOf(
        NavBarItem.Explore,
        NavBarItem.Map,
        NavBarItem.Library,
        NavBarItem.Profile,
    )
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        NavBarScreen(navController = navController, items = navBarItems)
    }) {it
        NavHost(navHostController = navController)
    }
}

sealed class NavBarItem(val title: String, val route: String, @DrawableRes val icon: Int) {
    object Explore: NavBarItem(
        title = "Explore",
        route = "explore_route",
        icon = R.drawable.baseline_travel_explore_24
    )

    object Map: NavBarItem(
        title = "Map",
        route = "map_route",
        icon = R.drawable.baseline_navigation_24
    )

    object Library: NavBarItem(
        title = "Library",
        route = "library_route",
        icon = R.drawable.baseline_saved_search_24
    )

    object Profile: NavBarItem(
        title = "Profile",
        route = "profile_route",
        icon = R.drawable.baseline_person_outline_24
    )
}

@Composable
fun NavBarScreen(navController: NavController, items: List<NavBarItem>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination= navBackStackEntry?.destination
    BottomNavigation {
        items.forEach {
                item ->
            BottomNavigationItem(
                selected = currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null
                    )
                },
                label = { Text(text = item.title) }
            )
        }
    }
}

@Composable
fun NavHost(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = NavBarItem.Explore.route) {
        composable(route = NavBarItem.Explore.route) {
            ExploreScreen()
        }
        composable(route = NavBarItem.Map.route) {
            MapScreen()
        }
        composable(route = NavBarItem.Library.route) {
            LibraryScreen()
        }
        composable(route = NavBarItem.Profile.route) {
            ProfileScreen()
        }
    }
}

@Composable
fun ExploreScreen() {
    Box(
        modifier= Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Explore")
    }
}

@Composable
fun MapScreen() {
    val lon = 24.9668
    val lat = 45.9432
    val x = (1.0 / 360.0) * (180 + lon)
    val y = (1.0 / 360.0) * (180 - lat)

    val tileStreamProvider =
        TileStreamProvider { row, col, zoomLvl ->
            try {
                val url = URL("https://api.maptiler.com/tiles/satellite-v2/${22-zoomLvl}/${col}/${row}.jpg?key=${BuildConfig.MAP_API_KEY}")
                val connection = url.openConnection() as HttpsURLConnection
                connection.doInput = true
                connection.connect()
                BufferedInputStream(connection.inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    val state: MapState by remember {
        mutableStateOf(
            MapState(17, 64 * 512, 64 * 512, 512, workerCount = 16) {
                scroll(x, y)
            }.apply {
                addLayer(tileStreamProvider)
                shouldLoopScale = true
                setPreloadingPadding(1 * 512)
            }
        )
    }

    MapUI(state = state, modifier = Modifier.fillMaxSize())
}
@Composable
fun LibraryScreen() {
    Box(
        modifier= Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Library")
    }
}
@Composable
fun ProfileScreen() {
    Box(
        modifier= Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Profile")
    }
}
