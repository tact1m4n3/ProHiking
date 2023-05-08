package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ovh.plrapps.mapcompose.ui.MapUI

@Composable
fun MapScreen(navController: NavController) {
    val viewModel = viewModel<MapViewModel>()
    MapUI(state = viewModel.state, modifier = Modifier.fillMaxSize())
}
