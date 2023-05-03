package com.anonymous.prohiking.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.anonymous.prohiking.BuildConfig
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.setPreloadingPadding
import ovh.plrapps.mapcompose.api.shouldLoopScale
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.MapUI
import ovh.plrapps.mapcompose.ui.state.MapState
import java.io.BufferedInputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@Composable
fun MapScreen() {
    val lon = 24.9668
    val lat = 45.9432
    val x = (1.0 / 360.0) * (180 + lon)
    val y = (1.0 / 360.0) * (180 - lat)

    val tileStreamProvider =
        TileStreamProvider { row, col, zoomLvl ->
            try {
                val url = URL("https://api.maptiler.com/tiles/satellite-v2/${22-zoomLvl}/${col}/${row}.jpg?key=${BuildConfig.MAP_API_KEY}")
                val connection = url.openConnection() as HttpsURLConnection
                connection.doInput = true
                connection.connect()
                BufferedInputStream(connection.inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    val state: MapState by remember {
        mutableStateOf(
            MapState(17, 64 * 512, 64 * 512, 512, workerCount = 16) {
                scroll(x, y)
            }.apply {
                addLayer(tileStreamProvider)
                shouldLoopScale = true
                setPreloadingPadding(1 * 512)
            }
        )
    }

    MapUI(state = state, modifier = Modifier.fillMaxSize())
}
