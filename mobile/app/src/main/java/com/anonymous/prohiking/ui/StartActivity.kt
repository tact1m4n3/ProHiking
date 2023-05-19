package com.anonymous.prohiking.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anonymous.prohiking.ui.start.LoginScreen
import com.anonymous.prohiking.ui.start.LoginViewModel
import com.anonymous.prohiking.ui.start.RegisterScreen
import com.anonymous.prohiking.ui.theme.ProHikingTheme

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE,
            ),
            0
        )

        setContent {
            val navController = rememberNavController()

            ProHikingTheme {
                NavigationGraph(navHostController = navController)
            }
        }
    }
}

@Composable
private fun NavigationGraph(navHostController: NavHostController) {
    val loginViewModel = viewModel<LoginViewModel>(factory = LoginViewModel.Factory)

    NavHost(navController = navHostController, startDestination = Screen.Start.Login.route) {
        composable(route = Screen.Start.Login.route) {
            LoginScreen(navController = navHostController, loginViewModel = loginViewModel)
        }
        composable(route = Screen.Start.Register.route) {
            RegisterScreen(navController = navHostController)
        }
    }
}
