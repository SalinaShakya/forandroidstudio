package com.example.myview.ui.compose


import android.location.Geocoder
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
fun ShippingEditScreen(
    onBack: () -> Unit = {},
    onSave: (String, String, String, String) -> Unit = { _, _, _, _ -> }
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // Error states
    var fullNameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }

    // Switch states
    var defaultShipping by remember { mutableStateOf(false) }
    var defaultBilling by remember { mutableStateOf(false) }

    var selectedLabel by remember { mutableStateOf("Home") }
    var showMap by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Edit your Address")
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
                            onValueChange = {
                                fullName = it
                                val words = it.trim().split(" ").filter { it.isNotEmpty() }
                                if (words.size >= 2) fullNameError = false
                            },
                            label = { Text("Enter Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g. Salina Shakya") },
                            shape = RoundedCornerShape(16.dp),
                            isError = fullNameError,
                            supportingText = {
                                if (fullNameError) {
                                    Text("Please enter both name and caste", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        )

                        // Mobile Number
                        Text("Mobile Number")

                        OutlinedTextField(
                            value = phone,
                            onValueChange = {
                                phone = it
                                if (it.length == 10 && (it.startsWith("98") || it.startsWith("97"))) {
                                    phoneError = false
                                }
                            },
                            label = { Text("Enter mobile No.") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            isError = phoneError,
                            supportingText = {
                                if (phoneError) {
                                    Text("Enter a valid 10-digit number (98/97)", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        )

                        // Address
                        Text("Address")

                        OutlinedTextField(
                            value = address,
                            onValueChange = {
                                address = it
                                if (it.isNotBlank()) addressError = false
                            },
                            label = { Text("Enter Address") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 1,
                            isError = addressError,
                            supportingText = {
                                if (addressError) {
                                    Text("Address is required", color = MaterialTheme.colorScheme.error)
                                }
                            },
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
                            val labels = listOf("Home", "Office", "Other")
                            labels.forEach { label ->
                                Button(
                                    onClick = { selectedLabel = label },
                                    Modifier.height(34.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedLabel == label) Color(0xFF4CAF50) else Color.Transparent,
                                        contentColor = if (selectedLabel == label) Color.White else Color.Gray
                                    ),
                                    border = if (selectedLabel != label) BorderStroke(1.dp, Color.LightGray) else null,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(label)
                                }
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Make this as a default shipping address",
                                    fontSize = 11.sp,
                                    color = Color(0xFF717282),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Switch(
                                    checked = defaultShipping,
                                    onCheckedChange = { defaultShipping = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(0xFF2ABB00),
                                        uncheckedThumbColor = Color.White,
                                        uncheckedTrackColor = Color(0xFF717282),
                                        uncheckedBorderColor = Color.Transparent
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Make this as a default billing address",
                                    fontSize = 11.sp,
                                    color = Color(0xFF717282),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Switch(
                                    checked = defaultBilling,
                                    onCheckedChange = { defaultBilling = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(0xFF2ABB00),
                                        uncheckedThumbColor = Color.White,
                                        uncheckedTrackColor = Color(0xFF717282),
                                        uncheckedBorderColor = Color.Transparent
                                    )
                                )
                            }
                        }
                        Text(
                            "Delete Address",
                            fontSize = 11.sp,
                            color = Color(0xFF717282),
                            fontWeight = FontWeight.SemiBold
                        )

                        // Save button
                        Button(
                            onClick = {
                                val nameWordsList = fullName.trim().split(" ").filter { it.isNotEmpty() }
                                val isNameValid = fullName.isNotBlank() && nameWordsList.size >= 2
                                val isPhoneValid = phone.isNotBlank() && phone.length == 10 && (phone.startsWith("98") || phone.startsWith("97"))
                                val isAddressValid = address.isNotBlank()

                                fullNameError = !isNameValid
                                phoneError = !isPhoneValid
                                addressError = !isAddressValid

                                if (isNameValid && isPhoneValid && isAddressValid) {
                                    onSave(fullName, phone, address, selectedLabel)
                                }
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
                    imageVector = Icons.Default.LocationOn,
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
                            val addresses = geocoder.getFromLocation(
                                center.latitude,
                                center.longitude,
                                1
                            )
                            address = addresses?.getOrNull(0)?.getAddressLine(0)
                                ?: "Lat: ${center.latitude}, Lon: ${center.longitude}"
                            addressError = false
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

@Preview(showBackground = true)
@Composable
fun ShippingEditScreen() {
    MaterialTheme {
        ShippingEditScreen()
    }
}
