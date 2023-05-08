package com.anonymous.prohiking.ui

sealed class Screen(val route: String) {
    sealed class Auth {
        object Login: Screen(route = "login_screen")
        object Register: Screen(route = "register_screen")
    }

    sealed class Main {
        object Explore: Screen(route = "explore_screen")
        object Map: Screen(route = "map_screen")
        object Library: Screen(route = "library_screen")
        object Profile: Screen(route = "profile_screen")
    }
}