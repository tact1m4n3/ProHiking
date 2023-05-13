package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.data.utils.hasLocationPermission
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun NavigateScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    navigateViewModel: NavigateViewModel = viewModel()
) {
    val context = LocalContext.current

    if (context.hasLocationPermission()) {
        val cameraPositionState = rememberCameraPositionState()

        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true,
                mapType = MapType.SATELLITE
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = true
            ),
            onMyLocationClick = {

            }
        ) {
        }
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            Text("Can't get your location", modifier = Modifier.align(Alignment.Center), fontSize = 32.sp)
        }
    }
}
