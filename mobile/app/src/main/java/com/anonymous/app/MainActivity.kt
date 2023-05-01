package com.example.bottomnavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.anonymous.app.ui.theme.Hiking4LifeTheme
import com.example.bottomnavigation.ui.theme.BottomNavHost
import com.example.bottomnavigation.ui.theme.BottomNavigationScreen
import com.example.bottomnavigation.ui.theme.Screens

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val listItems = listOf(
                Screens.Explore,
                Screens.Navigation,
                Screens.Saved,
                Screens.Profile
            )
            val navController = rememberNavController()
           Hiking4LifeTheme() {
                Surface(
                    modifier= Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    Scaffold(bottomBar = {
                        BottomNavigationScreen(navController = navController, items = listItems)
                    }) {it
                        BottomNavHost(navHostController = navController)

                    }
                }
            }
        }
    }
}

