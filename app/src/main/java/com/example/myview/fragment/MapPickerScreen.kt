package com.example.myview.fragment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun MapPicker() {
    val context = LocalContext.current

    // This is the "Bridge" that lets legacy OSM work in modern Compose
    AndroidView(
        factory = { ctx ->
            org.osmdroid.views.MapView(ctx).apply {
                setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
                setMultiTouchControls(true) // Allows zooming
                controller.setZoom(15.0)
                controller.setCenter(org.osmdroid.util.GeoPoint(27.6756, 85.3168)) // Initial view
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}