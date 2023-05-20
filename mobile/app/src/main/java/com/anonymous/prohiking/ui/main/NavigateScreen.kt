package com.anonymous.prohiking.ui.main

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.ui.widgets.ConfirmDialog
import com.anonymous.prohiking.ui.widgets.EmergencyButton
import com.anonymous.prohiking.utils.hasLocationPermission
import com.anonymous.prohiking.ui.widgets.GoogleMapsButton
import com.anonymous.prohiking.ui.widgets.TrailSymbol
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
    val currentTrail by navigateViewModel.currentTrail.collectAsState()
    val currentTrailPath by navigateViewModel.currentTrailPath.collectAsState()

    val romaniaBounds = LatLngBounds(
        LatLng(43.6884447292, 20.2201924985),
        LatLng(48.2208812526, 29.62654341)
    )

    val cameraPositionState = rememberCameraPositionState()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    if (context.hasLocationPermission()) {
        BottomSheetScaffold(
            sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onPrimaryContainer)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Current Location",
                            style = TextStyle(
                                fontSize = 25.sp,
                                letterSpacing = 0.8f.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.tertiary,
                                textDecoration = TextDecoration.Underline
                            )
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Latitude", fontSize = 18.sp)
                                Text(
                                    text = String.format("%.2f", location.latitude),
                                    fontSize = 25.sp
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Longitude", fontSize = 18.sp)
                                Text(
                                    text = String.format("%.2f", location.longitude),
                                    fontSize = 25.sp
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Altitude", fontSize = 18.sp)
                                Text(
                                    text = String.format("%.2f", location.altitude),
                                    fontSize = 25.sp
                                )
                            }
                        }

                        currentTrail?.let { trail ->
                            Text(
                                text = "Description",
                                style = TextStyle(
                                    fontSize = 25.sp,
                                    letterSpacing = (0.8).sp,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    textDecoration = TextDecoration.Underline
                                )
                            )

                            Box(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier
                                        .height(175.dp)
                                        .padding(15.dp),
                                ) {
                                    Text(
                                        text = "Trail",
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            letterSpacing = (0.8).sp,
                                            fontFamily = FontFamily.Default,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = trail.name,
                                        style = TextStyle(
                                            fontSize = 22.sp,
                                            fontFamily = FontFamily.SansSerif
                                        )
                                    )

                                    Spacer(modifier = Modifier.size(15.dp))

                                    Text(
                                        text = "Length",
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            letterSpacing = (0.8).sp,
                                            fontFamily = FontFamily.Default,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = "${trail.length} km",
                                        style = TextStyle(
                                            fontSize = 22.sp,
                                            fontFamily = FontFamily.Default
                                        )
                                    )
                                }

                                TrailSymbol(
                                    text = trail.symbol,
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .size(80.dp)
                                        .align(Alignment.BottomEnd)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                var showConfirmDialog by remember {
                                    mutableStateOf(false)
                                }

                                Button(
                                    shape = CircleShape,
                                    onClick = {
                                         showConfirmDialog = true
                                    },
                                ) {
                                    Text(
                                        "Stop Trail",
                                    )
                                }

                                if (showConfirmDialog) {
                                    ConfirmDialog(
                                        title = "Do you want to stop this trail?",
                                        onDismiss = {
                                            showConfirmDialog = false
                                        },
                                        onConfirm = {
                                            showConfirmDialog = false

                                            navigateViewModel.onStopTrailButtonClick()
                                        }
                                    )
                                }
                            }
                        } ?: run {
                            Spacer(modifier = Modifier.size(50.dp))
                        }

                        Spacer(modifier = Modifier.size(20.dp))
                    }
                }
            },
            sheetPeekHeight = 0.dp,
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
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
                                )
                            )
                        } ?: run {
                            map.animateCamera(CameraUpdateFactory.newLatLngBounds(
                                romaniaBounds,
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

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Spacer(modifier = Modifier.size(50.dp))

                    if (currentTrail != null) {
                        GoogleMapsButton(
                            modifier = Modifier.padding(top = 10.dp, end = 10.dp),
                            onClick = {
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
                    }

                    GoogleMapsButton(modifier = Modifier.padding(top = 10.dp, end = 10.dp), onClick = {
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

    EmergencyButton(
        modifier = modifier
            .padding(10.dp)
    )
}
