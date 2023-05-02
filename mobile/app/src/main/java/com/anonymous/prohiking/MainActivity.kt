package com.anonymous.prohiking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.anonymous.prohiking.ui.theme.ProHikingTheme
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.setPreloadingPadding
import ovh.plrapps.mapcompose.api.shouldLoopScale
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.MapUI
import ovh.plrapps.mapcompose.ui.state.MapState
import java.io.BufferedInputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import androidx.lifecycle.ViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProHikingTheme(darkTheme = true) {
                ProHikingApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProHikingApp() {
    val navController = rememberNavController()

    val navBarItems = listOf(
        Screen.Explore,
        Screen.Map,
        Screen.Library,
        Screen.Profile,
    )

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        NavBarScreen(navController = navController, items = navBarItems)
    }) {it
        NavHost(navController = navController, startDestination = Screen.Explore.route) {
            composable(route = Screen.Explore.route) {
                ExploreScreen()
            }
            composable(route = Screen.Map.route) {
                MapScreen()
            }
            composable(route = Screen.Library.route) {
                LibraryScreen()
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    ProHikingTheme {
        ProHikingApp()
    }
}

sealed class Screen(val route: String, @StringRes val title: Int, val icon: ImageVector) {
    object Explore: Screen(
        route = "explore",
        title = R.string.explore,
        icon = Icons.Outlined.Explore,
    )

    object Map: Screen(
        route = "map",
        title = R.string.map,
        icon = Icons.Outlined.Map
    )

    object Library: Screen(
        route = "library",
        title = R.string.library,
        icon = Icons.Outlined.LibraryBooks
    )

    object Profile: Screen(
        route = "profile",
        title = R.string.profile,
        icon = Icons.Outlined.People
    )
}

@Composable
fun NavBarScreen(navController: NavController, items: List<Screen>) {
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
                        item.icon,
                        contentDescription = null
                    )
                },
                label = { Text(text = stringResource(id = item.title)) }
            )
        }
    }
}

@Composable
fun ExploreScreen() {
    val viewModel = viewModel<ExploreViewModel>()
    val searchText by viewModel.searchText.collectAsState()
    val tracks by viewModel.tracks.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()


        Column(
            modifier= Modifier
                .fillMaxSize()
                .padding(16.dp)

        ){
            TextField(
                value = searchText,
                onValueChange =  viewModel::onSearchTextChange,
                modifier= Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Search")}
            )
            Spacer( modifier= Modifier.height(16.dp))
            if(isSearching)
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            LazyColumn(
                modifier= Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(tracks) {track ->
                    Text(
                        text="${track.trackName}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)

                    )


                }


            }
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