package com.anonymous.prohiking.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ovh.plrapps.mapcompose.ui.MapUI

@Composable
fun MapScreen() {
    val viewModel = viewModel<MapViewModel>()
    MapUI(state = viewModel.state, modifier = Modifier.fillMaxSize())
}
