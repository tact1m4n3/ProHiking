package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.anonymous.prohiking.R
import com.anonymous.prohiking.ui.Screen
import com.anonymous.prohiking.utils.ConnectionState
import com.anonymous.prohiking.utils.connectivityState

@Composable
fun BottomNavBarScreen(navController: NavController) {
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(backgroundColor = MaterialTheme.colorScheme.primary) {
        if (isConnected) {
            BottomNavigationItem(
                selected = currentDestination?.route == Screen.Main.Explore.route,
                onClick = { navigateTo(navController, Screen.Main.Explore.route) },
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
            onClick = { navigateTo(navController, Screen.Main.Navigate.route) },
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
            selected = currentDestination?.route == Screen.Main.Profile.route,
            onClick = { navigateTo(navController, Screen.Main.Profile.route) },
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

    var showOfflineDialog by remember { mutableStateOf(!isConnected) }

    if (showOfflineDialog) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { showOfflineDialog = false },
            title = {
                Text(text = "No internet connection", fontSize = 20.sp)
            },
            containerColor = MaterialTheme.colorScheme.onPrimary,
            confirmButton = {
                Button(
                    onClick = { showOfflineDialog = false },
                    shape = RoundedCornerShape(30.0f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(3.dp, Color(0.0f, 0.5f, 0.7f, 1.0f))
                ) {
                    Text("Understood", color = Color(0.0f, 0.5f, 0.7f, 1.0f))
                }
            }
        )
    }
}

private fun navigateTo(navController: NavController, route: String) {
    navController.currentDestination?.let { current ->
        if (current.route != route) {
            navController.navigate(route)
        }
    }
}