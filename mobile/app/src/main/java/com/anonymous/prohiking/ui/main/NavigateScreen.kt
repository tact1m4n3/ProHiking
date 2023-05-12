package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ovh.plrapps.mapcompose.ui.MapUI

@Composable
fun NavigateScreen(navController: NavController, viewModel: NavigateViewModel = viewModel(factory = NavigateViewModel.Factory)) {
    MapUI(state = viewModel.state, modifier = Modifier.fillMaxSize())
}
