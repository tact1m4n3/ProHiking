package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.data.utils.hasLocationPermission
import com.anonymous.prohiking.ui.Screen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun TrailsOnMapScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    exploreViewModel: ExploreViewModel = viewModel(factory = ExploreViewModel.Factory)
) {
    val context = LocalContext.current

    val romaniaBounds = LatLngBounds(
        LatLng(43.6884447292, 20.2201924985),
        LatLng(48.2208812526, 29.62654341)
    )

    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        while (exploreViewModel.fetchNextTrails(50, romaniaBounds.center, 100.0)) {
            println("fetched one")
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopAppBar(backgroundColor = MaterialTheme.colorScheme.primary) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Text(
                    text = "All Trails",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    softWrap = true,
                    maxLines = 1,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            if (context.hasLocationPermission()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        mapType = MapType.SATELLITE
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false
                    )
                ) {
                    var zoomedAlready by rememberSaveable { mutableStateOf(false) }
                    MapEffect(true) { map ->
                        if (!zoomedAlready) {
                            map.animateCamera(CameraUpdateFactory.newLatLngBounds(romaniaBounds, 100))
                            zoomedAlready = true
                        }
                    }

                    Clustering(
                        items = exploreViewModel.allTrails,
                        onClusterClick = { cluster ->
                            val bounds = LatLngBounds.builder()
                            cluster.items.onEach { trail ->
                                bounds.include(trail.position)
                            }
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngBounds(bounds.build(), 100)
                                )
                            }
                            false
                        },
                        onClusterItemInfoWindowClick = { trail ->
                            exploreViewModel.onTrailSelect(trail.inner)
                            coroutineScope.launch {
                                navController.navigate(Screen.Main.TrailDetails.route)
                            }
                        }
                    )
                }
            }
        }
    }
}
