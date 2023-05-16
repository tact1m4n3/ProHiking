package com.anonymous.prohiking.ui.main

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.CenterFocusWeak
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.R
import com.anonymous.prohiking.data.utils.hasLocationPermission
import com.anonymous.prohiking.ui.widgets.GoogleMapsButton
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import kotlinx.coroutines.launch
import androidx.compose.material.BottomSheetScaffold as BottomSheetScaffold

@OptIn(ExperimentalMaterialApi::class)
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
                        .height(260.dp)
                        .background(MaterialTheme.colorScheme.primary)
                ) {

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
                    navigateViewModel.focusOnCurrentTrail()
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
