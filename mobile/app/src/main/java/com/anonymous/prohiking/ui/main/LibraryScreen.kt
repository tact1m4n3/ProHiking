package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun LibraryScreen(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
    ){
        Text(text = "Library", modifier = Modifier.align(Alignment.Center))
    }
}
