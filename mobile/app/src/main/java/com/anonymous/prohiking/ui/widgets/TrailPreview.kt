package com.anonymous.prohiking.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.anonymous.prohiking.data.model.Point
import com.anonymous.prohiking.data.model.Trail
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.widgets.ScaleBar

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun TrailPreview(trail: Trail, trailPath: List<Point>, modifier: Modifier = Modifier) {
    if (trailPath.size < 2) {
        return
    }

    val bounds = LatLngBounds.builder().also { builder ->
        for (point in trailPath) {
            builder.include(point.let { LatLng(it.lat, it.lon) })
        }
    }.build()

    val cameraPositionState = rememberCameraPositionState()
    val startMarkerState = MarkerState(position = trailPath[0].let { LatLng(it.lat, it.lon) })
    val endMarkerState = MarkerState(position = trailPath[trailPath.size-1].let { LatLng(it.lat, it.lon) })

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = MapType.SATELLITE
            )
        ) {
            MapEffect(trailPath) {map ->
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    100
                ))
            }

            Marker(state = startMarkerState, title = trail.from)
            Marker(state = endMarkerState, title = trail.to)
            Polyline(points = trailPath.map { LatLng(it.lat, it.lon) }, color = Color(0xFF2596BE))
        }

        ScaleBar(
            modifier = Modifier
                .padding(top = 5.dp, end = 15.dp)
                .align(Alignment.TopStart),
            cameraPositionState = cameraPositionState,
            textColor = Color.White
        )
    }
}
