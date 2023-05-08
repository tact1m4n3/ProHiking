package com.anonymous.prohiking.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.anonymous.prohiking.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.setPreloadingPadding
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.state.MapState
import java.io.BufferedInputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MapViewModel : ViewModel() {
    private val tileStreamProvider =
        TileStreamProvider { row, col, zoomLvl ->
            try {
                val url = URL("https://api.maptiler.com/tiles/satellite-v2/${zoomLvl}/${col}/${row}.jpg?key=${BuildConfig.MAP_API_KEY}")
                val connection = withContext(Dispatchers.IO) {
                    url.openConnection()
                } as HttpsURLConnection
                connection.doInput = true
                withContext(Dispatchers.IO) {
                    connection.connect()
                }
                BufferedInputStream(connection.inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    val state: MapState by mutableStateOf(
        MapState(16, 180 * 180 * 512, 180 * 180 * 512, 512, workerCount = 16) {
            val lon = 24.9668
            val lat = 45.9432
            val x = (1.0 / 360.0) * (180 + lon)
            val y = (1.0 / 360.0) * (180 - lat)

            scroll(x, y)
            scale(0f)
        }.apply {
            addLayer(tileStreamProvider)
            setPreloadingPadding(1 * 512)
        }
    )
}
