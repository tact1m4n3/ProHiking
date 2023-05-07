package com.anonymous.prohiking.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.anonymous.prohiking.R
import com.anonymous.prohiking.ui.NavigationRoutes

@Composable
fun BottomNavBarScreen(navController: NavController) {
    val bottomBarVisibleState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    when (currentDestination?.route) {
        NavigationRoutes.LoginScreen.route -> bottomBarVisibleState.value = false
        NavigationRoutes.RegisterScreen.route -> bottomBarVisibleState.value = false
        else -> bottomBarVisibleState.value = true
    }

    AnimatedVisibility(
        visible = bottomBarVisibleState.value,
        enter = fadeIn(initialAlpha = 0f),
        exit = fadeOut(targetAlpha = 0f),
        content = {
            BottomNavigation(backgroundColor = MaterialTheme.colorScheme.primary) {
                BottomNavigationItem(
                    selected = currentDestination?.route == NavigationRoutes.ExploreScreen.route,
                    onClick = { navController.navigate(NavigationRoutes.ExploreScreen.route) },
                    icon = {
                        Icon(
                            Icons.Outlined.Explore,
                            contentDescription = null
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.explore)) }
                )
                BottomNavigationItem(
                    selected = currentDestination?.route == NavigationRoutes.MapScreen.route,
                    onClick = { navController.navigate(NavigationRoutes.MapScreen.route) },
                    icon = {
                        Icon(
                            Icons.Outlined.Map,
                            contentDescription = null
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.map)) }
                )
                BottomNavigationItem(
                    selected = currentDestination?.route == NavigationRoutes.LibraryScreen.route,
                    onClick = { navController.navigate(NavigationRoutes.LibraryScreen.route) },
                    icon = {
                        Icon(
                            Icons.Outlined.LibraryBooks,
                            contentDescription = null
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.library)) }
                )
                BottomNavigationItem(
                    selected = currentDestination?.route == NavigationRoutes.ProfileScreen.route,
                    onClick = { navController.navigate(NavigationRoutes.ProfileScreen.route) },
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
    )
}
