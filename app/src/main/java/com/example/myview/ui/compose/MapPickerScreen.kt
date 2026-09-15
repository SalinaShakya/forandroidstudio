package com.example.myview.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun MapPicker() {
    val context = LocalContext.current

    // This is the "Bridge" that lets legacy OSM work in modern Compose
    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true) // Allows zooming
                controller.setZoom(15.0)
                controller.setCenter(GeoPoint(27.6756, 85.3168)) // Initial view
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}