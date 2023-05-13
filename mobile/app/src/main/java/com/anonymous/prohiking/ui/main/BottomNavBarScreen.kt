package com.anonymous.prohiking.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Navigation
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
import com.anonymous.prohiking.ui.Screen

@Composable
fun BottomNavBarScreen(navController: NavController) {
    val bottomBarVisibleState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AnimatedVisibility(
        visible = bottomBarVisibleState.value,
        enter = fadeIn(initialAlpha = 0f),
        exit = fadeOut(targetAlpha = 0f),
        content = {
            BottomNavigation(backgroundColor = MaterialTheme.colorScheme.primary) {
                BottomNavigationItem(
                    selected = currentDestination?.route == Screen.Main.Explore.route,
                    onClick = { navController.navigate(Screen.Main.Explore.route) },
                    icon = {
                        Icon(
                            Icons.Outlined.Explore,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentDescription = null
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.explore), color = MaterialTheme.colorScheme.onPrimaryContainer) }
                )
                BottomNavigationItem(
                    selected = currentDestination?.route == Screen.Main.Navigate.route,
                    onClick = { navController.navigate(Screen.Main.Navigate.route) },
                    icon = {
                        Icon(
                            Icons.Outlined.Navigation,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentDescription = null,

                        )
                    },
                    label = { Text(text = stringResource(id = R.string.navigate), color = MaterialTheme.colorScheme.onPrimaryContainer) }
                )
                BottomNavigationItem(
                    selected = currentDestination?.route == Screen.Main.Library.route,
                    onClick = { navController.navigate(Screen.Main.Library.route) },
                    icon = {
                        Icon(
                            Icons.Outlined.LibraryBooks,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentDescription = null
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.library), color = MaterialTheme.colorScheme.onPrimaryContainer) }
                )
                BottomNavigationItem(
                    selected = currentDestination?.route == Screen.Main.Profile.route,
                    onClick = { navController.navigate(Screen.Main.Profile.route) },
                    icon = {
                        Icon(
                            Icons.Outlined.People,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentDescription = null
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.profile), color = MaterialTheme.colorScheme.onPrimaryContainer) }
                )
            }
        }
    )
}