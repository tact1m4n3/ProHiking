package com.anonymous.prohiking.ui.screens

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.anonymous.prohiking.R
import com.anonymous.prohiking.ui.NavDestinations

@Composable
fun BottomNavBarScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        BottomNavigationItem(
            selected = currentDestination?.route == NavDestinations.ExploreScreen.route,
            onClick = { navController.navigate(NavDestinations.ExploreScreen.route) },
            icon = {
                Icon(
                    Icons.Outlined.Explore,
                    contentDescription = null
                )
            },
            label = { Text(text = stringResource(id = R.string.explore)) }
        )
        BottomNavigationItem(
            selected = currentDestination?.route == NavDestinations.MapScreen.route,
            onClick = { navController.navigate(NavDestinations.MapScreen.route) },
            icon = {
                Icon(
                    Icons.Outlined.Map,
                    contentDescription = null
                )
            },
            label = { Text(text = stringResource(id = R.string.map)) }
        )
        BottomNavigationItem(
            selected = currentDestination?.route == NavDestinations.LibraryScreen.route,
            onClick = { navController.navigate(NavDestinations.LibraryScreen.route) },
            icon = {
                Icon(
                    Icons.Outlined.LibraryBooks,
                    contentDescription = null
                )
            },
            label = { Text(text = stringResource(id = R.string.library)) }
        )
        BottomNavigationItem(
            selected = currentDestination?.route == NavDestinations.ProfileScreen.route,
            onClick = { navController.navigate(NavDestinations.ProfileScreen.route) },
            icon = {
                Icon(
                    Icons.Outlined.People,
                    contentDescription = null
                )
            },
            label = { Text(text = stringResource(id = R.string.profile)) }
        )
    }
}
