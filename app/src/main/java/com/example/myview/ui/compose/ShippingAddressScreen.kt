package com.example.myview.ui.compose

import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myview.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingAddressScreen(
    onBack: () -> Unit = {},
    onSave: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // Switch states
    var defaultShipping by remember { mutableStateOf(false) }
    var defaultBilling by remember { mutableStateOf(false) }

    var showMap by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Add your new address")
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFF7F7F7))
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // White rounded box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Heading
                        Text(
                            text = "Details for shipping",
                            style = MaterialTheme.typography.titleMedium
                        )

                        // Full Name
                        Text("Full Name")

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Enter Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )

                        // Mobile Number
                        Text("Mobile Number")

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Enter mobile No.") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Address
                        Text("Address")

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Enter Address") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 1,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_shippping_address),
                                    contentDescription = "Location Icon",
                                    tint = Color.Gray,
                                    modifier = Modifier.clickable { showMap = true }
                                )
                            }
                        )

                        // Label
                        Text("Select a label")

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) {
                                Text("Home")
                            }

                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) {
                                Text("Office")
                            }

                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) {
                                Text("Other")
                            }
                        }

                        // Default Shipping
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Make this as a default shipping address",
                                fontSize = 10.sp,
                                modifier = Modifier.weight(1f)
                            )

                            CustomSwitch(
                                checked = defaultShipping,
                                onCheckedChange = {
                                    defaultShipping = it
                                }
                            )
                        }

                        // Default Billing
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Make this as a default billing address",
                                fontSize = 10.sp,
                                modifier = Modifier.weight(1f)
                            )

                            CustomSwitch(
                                checked = defaultBilling,
                                onCheckedChange = {
                                    defaultBilling = it
                                }
                            )
                        }
                    }
                }

                // Save button
                Button(
                    onClick = {
                        onSave(fullName, phone, address)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("SAVE", fontWeight = FontWeight.Bold)
                }
            }
        }

        // THE MAP OVERLAY
        if (showMap) {
            var mapViewInstance: MapView? by remember { mutableStateOf(null) }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                AndroidView(
                    factory = { context ->
                        MapView(context).apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            setMultiTouchControls(true)
                            controller.setZoom(15.0)
                            controller.setCenter(GeoPoint(27.6756, 85.3168))
                            mapViewInstance = this
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Center Pin (Indicator)
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.LocationOn,
                    contentDescription = "Center Indicator",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                        .padding(bottom = 25.dp) // Lift it so the tip is at the center
                )

                // Confirm Button
                Button(
                    onClick = {
                        val center = mapViewInstance?.mapCenter as? GeoPoint
                        if (center != null) {
                            val geocoder = Geocoder(context, Locale.getDefault())
                            val addresses = geocoder.getFromLocation(center.latitude, center.longitude, 1)
                            address = addresses?.getOrNull(0)?.getAddressLine(0) ?: "Lat: ${center.latitude}, Lon: ${center.longitude}"
                            showMap = false
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("CONFIRM LOCATION", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .height(50.dp)
            .clickable {
                onCheckedChange(!checked)
            },
        contentAlignment = Alignment.Center
    ) {
        // Background pill
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(50))
                .background(
                    if (checked)
                        Color(0xFFB9E8AE)
                    else
                        Color(0xFFA6A6A6)
                )
        )

        // Circle
        Box(
            modifier = Modifier
                .size(42.dp)
                .align(
                    if (checked)
                        Alignment.CenterEnd
                    else
                        Alignment.CenterStart
                )
                .clip(CircleShape)
                .background(
                    if (checked)
                        Color(0xFF18C000)
                    else
                        Color(0xFF202020)
                )
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShippingAddressPreview() {
    MaterialTheme {
        ShippingAddressScreen()
    }
}
