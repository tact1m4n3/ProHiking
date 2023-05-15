package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.data.utils.hasLocationPermission
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Polyline

@Composable
fun NavigateScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    navigateViewModel: NavigateViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentTrail by navigateViewModel.currentTrail.collectAsState()
    val currentTrailPath by navigateViewModel.currentTrailPath.collectAsState()
    val cameraPositionState = navigateViewModel.cameraPositionState

    if (context.hasLocationPermission()) {
        Box(modifier = modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
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
                currentTrailPath?.let { trailPath ->
                    Polyline(points = trailPath.map { LatLng(it.lat, it.lon) }, color = Color(0xFF2596BE))
                }
            }

            Box(modifier = Modifier
                .padding(10.dp)
                .padding(top = 50.dp)
                .size(40.dp)
                .background(Color(0xAAFFFFFF), RoundedCornerShape(4.dp))
                .align(Alignment.TopEnd)
            ) {
                IconButton(onClick = {
                    navigateViewModel.focusOnCurrentTrail()
                }, modifier = Modifier.matchParentSize()) {
                    Icon(
                        imageVector = Icons.Outlined.Map,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center),
                        tint = Color(0xFF444444)
                    )
                }
            }
        }
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            Text("Can't get your location", modifier = Modifier.align(Alignment.Center), fontSize = 32.sp)
        }
    }
}
