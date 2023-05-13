package com.anonymous.prohiking.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anonymous.prohiking.ui.auth.LoginScreen
import com.anonymous.prohiking.ui.auth.LoginViewModel
import com.anonymous.prohiking.ui.auth.RegisterScreen
import com.anonymous.prohiking.ui.theme.ProHikingTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    NavHost(navController = navHostController, startDestination = Screen.Auth.Login.route) {
        composable(route = Screen.Auth.Login.route) {
            LoginScreen(navController = navHostController, loginViewModel = loginViewModel)
        }
        composable(route = Screen.Auth.Register.route) {
            RegisterScreen(navController = navHostController)
        }
    }
}
