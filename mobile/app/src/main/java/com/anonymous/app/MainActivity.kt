package com.anonymous.app

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ApplicationInfoFlags
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.anonymous.app.ui.theme.AppTheme
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.setPreloadingPadding
import ovh.plrapps.mapcompose.api.shouldLoopScale
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.MapUI
import ovh.plrapps.mapcompose.ui.state.MapState
import java.io.BufferedInputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }
}

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

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val lon = 24.9668
    val lat = 45.9432
    val x = (1.0 / 360.0) * (180 + lon)
    val y = (1.0 / 360.0) * (180 - lat)

    val state: MapState by remember {
        mutableStateOf(
            MapState(18, 32 * 512, 32 * 512, 512, workerCount = 16) {
                scroll(x, y)
            }.apply {
                addLayer(tileStreamProvider)
                shouldLoopScale = true
                setPreloadingPadding(2 * 512)
            }
        )
    }

    MapUI(state = state, modifier = modifier.fillMaxSize())
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        MainScreen()
    }
}