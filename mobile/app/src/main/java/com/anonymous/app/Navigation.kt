package com.example.bottomnavigation.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bottomnavigation.Explore
import com.example.bottomnavigation.Navigation
import com.example.bottomnavigation.Profile
import com.example.bottomnavigation.R
import com.example.bottomnavigation.Saved
import com.example.bottomnavigation.ui.theme.Screens
import com.example.bottomnavigation.ui.theme.Screens as Screens1

sealed class Screens( val title: String, val route: String, @DrawableRes val icons: Int) {
    object Explore: Screens(
        title = "Explore",
        route = "explore_route",
        icons = R.drawable.baseline_travel_explore_24
    )
    object Navigation: Screens(
        title = "Navigation",
        route = "navigation_route",
        icons = R.drawable.baseline_navigation_24
    )
    object Saved: Screens(
        title = "Saved",
        route = "saved_route",
        icons = R.drawable.baseline_saved_search_24
    )
    object Profile: Screens(
        title = "Profile",
        route = "profile_route",
        icons = R.drawable.baseline_person_outline_24
    )

}

@Composable
fun BottomNavHost(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screens.Explore.route) {
        composable(route= Screens.Explore.route) {
            Explore()
        }
        composable(route= Screens.Navigation.route) {
            Navigation()
        }
        composable(route= Screens.Saved.route) {
            Saved()
        }
        composable(route= Screens.Profile.route) {
            Profile()
        }
    }
}

@Composable
fun BottomNavigationScreen(navController: NavController, items:List<Screens1>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination= navBackStackEntry?.destination
    BottomNavigation {
        items.forEach {
                screens ->
            BottomNavigationItem(
                selected = currentDestination?.route == screens.route,
                onClick = {
                    navController.navigate(screens.route)
                },
                icon = {
                    Icon(
                        painter= painterResource(id = screens.icons),
                        contentDescription = null
                    )
                },
                label = { Text(text = screens.title)}

            )
        }

    }
}
