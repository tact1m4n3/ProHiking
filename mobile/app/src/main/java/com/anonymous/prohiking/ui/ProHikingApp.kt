package com.anonymous.prohiking.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anonymous.prohiking.ui.screens.BottomNavBarScreen
import com.anonymous.prohiking.ui.screens.ExploreScreen
import com.anonymous.prohiking.ui.screens.LibraryScreen
import com.anonymous.prohiking.ui.screens.MapScreen
import com.anonymous.prohiking.ui.screens.ProfileScreen
import com.anonymous.prohiking.ui.theme.ProHikingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProHikingApp() {
    val navController = rememberNavController()

    ProHikingTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
            BottomNavBarScreen(navController = navController)
        }) {it
            NavigationGraph(navHostController = navController)
        }
    }
}

enum class NavDestinations(val route: String) {
    ExploreScreen("explore_screen"),
    MapScreen("map_screen"),
    LibraryScreen("library_screen"),
    ProfileScreen("profile_screen")
}

@Composable
fun NavigationGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = NavDestinations.ExploreScreen.route) {
        composable(route = NavDestinations.ExploreScreen.route) {
            ExploreScreen()
        }
        composable(route = NavDestinations.MapScreen.route) {
            MapScreen()
        }
        composable(route = NavDestinations.LibraryScreen.route) {
            LibraryScreen()
        }
        composable(route = NavDestinations.ProfileScreen.route) {
            ProfileScreen()
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
