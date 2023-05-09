package com.anonymous.prohiking.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anonymous.prohiking.ui.main.BottomNavBarScreen
import com.anonymous.prohiking.ui.main.ExploreScreen
import com.anonymous.prohiking.ui.main.LibraryScreen
import com.anonymous.prohiking.ui.main.MapScreen
import com.anonymous.prohiking.ui.main.ProfileScreen
import com.anonymous.prohiking.ui.theme.ProHikingTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            ProHikingTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    BottomNavBarScreen(navController = navController)
                }) { it
                    NavigationGraph(navHostController = navController)
                }
            }
        }
    }
}

@Composable
private fun NavigationGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screen.Main.Profile.route) {
        composable(route = Screen.Main.Explore.route) {
            ExploreScreen(navController = navHostController)
        }
        composable(route = Screen.Main.Map.route) {
            MapScreen(navController = navHostController)
        }
        composable(route = Screen.Main.Library.route) {
            LibraryScreen(navController = navHostController)
        }
        composable(route = Screen.Main.Profile.route) {
            ProfileScreen(navController = navHostController)
        }
    }
}
