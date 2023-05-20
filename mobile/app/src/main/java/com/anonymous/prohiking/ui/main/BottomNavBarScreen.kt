package com.anonymous.prohiking.ui.main

import android.annotation.SuppressLint
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.anonymous.prohiking.R
import com.anonymous.prohiking.ui.Screen
import com.anonymous.prohiking.utils.hasInternetConnection

@SuppressLint("UnrememberedMutableState")
@Composable
fun BottomNavBarScreen(navController: NavController) {
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(backgroundColor = MaterialTheme.colorScheme.primary) {
        if (context.hasInternetConnection()) {
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
        }
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
//                BottomNavigationItem(
//                    selected = currentDestination?.route == Screen.Main.Library.route,
//                    onClick = { navController.navigate(Screen.Main.Library.route) },
//                    icon = {
//                        Icon(
//                            Icons.Outlined.LibraryBooks,
//                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
//                            contentDescription = null
//                        )
//                    },
//                    label = { Text(text = stringResource(id = R.string.library), color = MaterialTheme.colorScheme.onPrimaryContainer) }
//                )
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