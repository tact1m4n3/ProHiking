package com.anonymous.prohiking.ui.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.CenterFocusWeak
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.data.utils.hasLocationPermission
import com.anonymous.prohiking.ui.widgets.GoogleMapsButton
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.sql.Time
import androidx.compose.material.BottomSheetScaffold as BottomSheetScaffold

@OptIn(ExperimentalMaterialApi::class, MapsComposeExperimentalApi::class)
@Composable
fun NavigateScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    navigateViewModel: NavigateViewModel = viewModel()
) {
    val context = LocalContext.current
    val location by navigateViewModel.location.collectAsState()
    val currentTrailTime by navigateViewModel.currentTrailTime.collectAsState()
    val currentTrail by navigateViewModel.currentTrail.collectAsState()
    val currentTrailPath by navigateViewModel.currentTrailPath.collectAsState()

    val cameraPositionState = rememberCameraPositionState()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    if (context.hasLocationPermission()) {
        BottomSheetScaffold(
            sheetShape = RoundedCornerShape(15.dp),
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Latitude", color = Color.Gray, fontSize = 15.sp)
                                Text(
                                    text = String.format("%.2f", location.latitude),
                                    color = Color.White,
                                    fontSize = 25.sp
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Longitude", color = Color.Gray, fontSize = 15.sp)
                                Text(
                                    text = String.format("%.2f", location.longitude),
                                    color = Color.White,
                                    fontSize = 25.sp
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Altitude", color = Color.Gray, fontSize = 15.sp)
                                Text(
                                    text = String.format("%.2f", location.altitude),
                                    color = Color.White,
                                    fontSize = 25.sp
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.size(10.dp))

                        if (currentTrailTime != -1L) {
                            Box(modifier = Modifier
                                .size(100.dp)
                                .background(Color.Black, CircleShape)
                            ) {
                                Text(
                                    text = Time(currentTrailTime).toString(),
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.size(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                onClick = {
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .weight(weight = 1f, fill = false),
                                    imageVector = Icons.Outlined.Pause,
                                    contentDescription = "Pause",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Button(
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                onClick = {
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .weight(weight = 1f, fill = false),
                                    imageVector = Icons.Outlined.Stop,
                                    contentDescription = "Stop",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Button(
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                onClick = {
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .weight(weight = 1f, fill = false),
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Save",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }, sheetPeekHeight = 0.dp
        ) {
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = true,
                        mapType = MapType.SATELLITE
                    ),
                    uiSettings = MapUiSettings(
                        myLocationButtonEnabled = true,
                        zoomControlsEnabled = false
                    ),
                    onMyLocationClick = {

                    }
                ) {
                    MapEffect(currentTrailPath) { map ->
                        currentTrailPath?.let { trailPath ->
                            val bounds = LatLngBounds.builder().also { builder ->
                                for (point in trailPath) {
                                    builder.include(point.let { LatLng(it.lat, it.lon) })
                                }
                            }.build()

                            map.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(
                                    bounds,
                                    100
                                ))
                        }
                    }

                    currentTrail?.let { trail ->
                        currentTrailPath?.let { trailPath ->
                            Marker(
                                state = MarkerState(position = trailPath[0].let { LatLng(it.lat, it.lon) }),
                                title = trail.from
                            )

                            Marker(
                                state = MarkerState(position = trailPath[trailPath.size-1].let { LatLng(it.lat, it.lon) }),
                                title = trail.to
                            )

                            Polyline(
                                points = trailPath.map { LatLng(it.lat, it.lon) },
                                color = Color(0xFF2596BE)
                            )
                        }
                    }
                }
                
                GoogleMapsButton(onClick = {
                    currentTrailPath?.let { trailPath ->
                        val bounds = LatLngBounds.builder().also { builder ->
                            for (point in trailPath) {
                                builder.include(point.let { LatLng(it.lat, it.lon) })
                            }
                        }.build()

                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngBounds(
                                    bounds,
                                    100
                                )
                            )
                        }
                    }
                }) { modifier, color ->
                    Icon(
                        imageVector = Icons.Outlined.CenterFocusWeak,
                        contentDescription = null,
                        modifier = modifier,
                        tint = color
                    )
                }

                GoogleMapsButton(modifier = Modifier.padding(top = 50.dp), onClick = {
                    coroutineScope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        } else {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
                }) { modifier, color ->
                    Crossfade(targetState = bottomSheetScaffoldState.bottomSheetState.isCollapsed) { isCollapsed ->
                        if (isCollapsed) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowUpward,
                                contentDescription = null,
                                modifier = modifier,
                                tint = color
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.ArrowDownward,
                                contentDescription = null,
                                modifier = modifier,
                                tint = color
                            )
                        }
                    }
                }
            }
        }
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            Text(
                "Can't get your location",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 32.sp
            )
        }
    }
}
