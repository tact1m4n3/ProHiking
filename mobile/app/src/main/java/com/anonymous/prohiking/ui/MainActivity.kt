package com.anonymous.prohiking.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anonymous.prohiking.ui.main.AccountScreen
import com.anonymous.prohiking.ui.main.BottomNavBarScreen
import com.anonymous.prohiking.ui.main.ContactScreen
import com.anonymous.prohiking.ui.main.ExploreScreen
import com.anonymous.prohiking.ui.main.ExploreViewModel
import com.anonymous.prohiking.ui.main.NavigateScreen
import com.anonymous.prohiking.ui.main.NavigateViewModel
import com.anonymous.prohiking.ui.main.ProfileScreen
import com.anonymous.prohiking.ui.main.ProfileViewModel
import com.anonymous.prohiking.ui.main.TrailDetailsScreen
import com.anonymous.prohiking.ui.main.TrailsOnMapScreen
import com.anonymous.prohiking.ui.theme.ProHikingTheme
import com.anonymous.prohiking.utils.ConnectionState
import com.anonymous.prohiking.utils.currentConnectivityState

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            ProHikingTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    BottomNavBarScreen(navController = navController)
                }) {
                    NavigationGraph(
                        navHostController = navController,
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}

@Composable
private fun NavigationGraph(navHostController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val exploreViewModel = viewModel<ExploreViewModel>(factory = ExploreViewModel.Factory)
    val navigateViewModel = viewModel<NavigateViewModel>(factory = NavigateViewModel.Factory)
    val profileViewModel = viewModel<ProfileViewModel>(factory = ProfileViewModel.Factory)

    NavHost(
        navController = navHostController,
        startDestination =
        if (context.currentConnectivityState == ConnectionState.Available)
            Screen.Main.Explore.route
        else
            Screen.Main.Navigate.route
    ) {
        composable(route = Screen.Main.Explore.route) {
            ExploreScreen(
                navController = navHostController,
                exploreViewModel = exploreViewModel,
                modifier = modifier
            )
        }
        composable(route = Screen.Main.TrailsOnMap.route) {
            TrailsOnMapScreen(
                navController = navHostController,
                exploreViewModel = exploreViewModel,
                modifier = modifier
            )
        }
        composable(route = Screen.Main.TrailDetails.route) {
            TrailDetailsScreen(
                navController = navHostController,
                exploreViewModel = exploreViewModel,
                modifier = modifier
            )
        }
        composable(route = Screen.Main.Navigate.route) {
            NavigateScreen(
                navController = navHostController,
                navigateViewModel = navigateViewModel,
                modifier = modifier
            )
        }
        composable(route = Screen.Main.Profile.route) {
            ProfileScreen(
                navController = navHostController,
                profileViewModel = profileViewModel,
                modifier = modifier
            )
        }
        composable(route = Screen.Main.Account.route) {
            AccountScreen(
                navController = navHostController,
                profileViewModel = profileViewModel,
                modifier = modifier
            )
        }
        composable(route = Screen.Main.Contact.route) {
            ContactScreen(
                navController = navHostController,
                profileViewModel = profileViewModel,
                modifier = modifier
            )
        }
    }
}
