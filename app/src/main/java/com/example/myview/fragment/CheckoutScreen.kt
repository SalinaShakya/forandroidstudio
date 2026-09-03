package com.example.myview.fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myview.R
import com.example.myview.data.local.FavoriteEntity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.myview.data.model.CartItem
import com.example.myview.ui.viewmodel.CheckoutViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    onBackClick: () -> Unit = {} ,
    onAddAddressClick: () -> Unit = {} // New parameter for redirection
) {
    val selectedPayment by viewModel.selectedPayment.collectAsState()
    val address by viewModel.deliveryAddress.collectAsState()

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true

    )
    var showMap by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = null,
                        modifier = Modifier.size(180.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "No address added yet !",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF182B3C)
                )

                Text(
                    text = "You have not added any shipping address.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Primary "SET ADDRESS" Button
                Button(
                    onClick = {
                        showSheet = false
                        showMap = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ABB00)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "SET ADDRESS", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Secondary "CANCEL" Button
                TextButton(onClick = { showSheet = false }) {
                    Text(text = "CANCEL", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showMap) {
        var mapViewInstance: MapView? by remember { mutableStateOf(null) }
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(15.0)
                        controller.setCenter(GeoPoint(27.6756, 85.3168)) // Initial center (e.g. Kathmandu)
//slidable
                        addMapListener(object : org.osmdroid.events.MapListener {
                            override fun onScroll(event: org.osmdroid.events.ScrollEvent?): Boolean {
                                // Update the instance reference every time the map moves
                                mapViewInstance = this@apply
                                return true
                            }
                            override fun onZoom(event: org.osmdroid.events.ZoomEvent?): Boolean = true

                        })
//TILL HERE
                        mapViewInstance = this //NNEEEEDDDD

                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Fixed Pin in the center
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(40.dp)
                )
            }

            // Confirm Button
            Button(
                onClick = {
                    val center = mapViewInstance?.mapCenter as? GeoPoint
                    if (center != null) {
                        viewModel.updateAddress(context,center.latitude, center.longitude)
                        showMap = false
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ABB00)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("CONFIRM LOCATION", fontWeight = FontWeight.Bold)
            }
        }
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Checkout", style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(painter = painterResource(id = R.drawable.back_arrow), contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = {
                GrandTotal(price = "${viewModel.getGrandTotal()}")
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF7F7F7)) // Grey background for the page
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Delivery Address Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(painter = painterResource(id = R.drawable.rectangle), contentDescription = null, modifier = Modifier.size(50.dp))
                            Image(painter = painterResource(id = R.drawable.location), contentDescription = null, modifier = Modifier.size(38.dp))
//                            Image(
//                                painter = painterResource(id = R.drawable.ic_checkoutplus),
//                                contentDescription = "Add Address",
//                                modifier = Modifier
//                                    .size(38.dp)
//                                    .offset(x = 270.dp)
//                                    .clickable { showSheet = true }
//                            )
                            //to change into the pencil thing using if else
                            Image(  painter = painterResource( id = if (address == "Delivery Address Not Set") R.drawable.ic_checkoutplus else R.drawable.ic_edit ),
                                contentDescription = "Add Address",
                                modifier = Modifier
                                    .size(38.dp)
                                    .offset(x = 280.dp)
                                    .clickable { showSheet = true }
                            )
                        }

                        Column(modifier = Modifier.padding(start = 12.dp,), //

                        ) {
                            Text(text = address,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF808080))
//                            Text(text = if (address == "Delivery Address Not Set") "Add Shipping Address" else
//                                "Delivery Address", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF182B3C))
//
                            Text(
                                text = if (address == "Delivery Address Not Set") "Add Shipping Address" else "Delivery Address",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF182B3C),
                                modifier = Modifier.clickable {
                                    // Trigger redirection ONLY when clicking this text and address is not set
                                    if (address == "Delivery Address Not Set") {
                                        onAddAddressClick()
                                    }
                                }
                            )

//
//                            Text(text = if (address == "Delivery Address Not Set") "Add Shipping Address" else
//                                "Delivery Address", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF182B3C))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Order Summary (${viewModel.cartItems.sumOf { it.quantity}})", fontSize = 15.sp, modifier = Modifier.padding(start = 4.dp))
                Spacer(modifier = Modifier.height(10.dp))

                //to create a separate box for each item in the cart
                viewModel.cartItems.forEach { item ->
                    Spacer(modifier = Modifier.height(12.dp)) // The gap between the boxes
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        // Box provides the internal side padding for the product row
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            OrderItemRow(item)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(40.dp)
                        .border(1.dp, Color(0xFF2ABB00), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "HAVE A PROMOCODE?",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2ABB00),
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Choose Your Payment Option", fontSize = 15.sp, modifier = Modifier.padding(start = 1.dp))
                Spacer(modifier = Modifier.height(5.dp))

                // Payment Options Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    ) {
                        PaymentItem(
                            icon = R.drawable.ic_payment,
                            label = "Cash on Delivery",
                            isSelected = selectedPayment == "Cash on Delivery",
                            onClick = { viewModel.selectPayment("Cash on Delivery") }
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        PaymentItem(
                            icon = R.drawable.ic_brand,
                            label = "Pay with eSewa",
                            isSelected = selectedPayment == "Pay with eSewa",
                            onClick = { viewModel.selectPayment("Pay with eSewa") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Security Section
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.ic_verified), contentDescription = null, modifier = Modifier.size(width = 40.dp, height = 30.dp))
                    Column(modifier = Modifier.padding(start = 1.dp)) {
                        Text(text = "SAFE AND SECURE PAYMENTS.", fontSize = 10.sp, fontWeight = FontWeight.Medium,modifier = Modifier.offset(y = (3).dp))
                        Text(text = "100% AUTHENTIC PRODUCTS.", fontSize = 10.sp, fontWeight = FontWeight.Medium,modifier = Modifier.offset(y = (-3).dp))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun OrderItemRow(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Image Container with grey background
        Surface(
            modifier = Modifier.size(70.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF2F2F2)
        ) {
            GlideImage(
                model = item.image,
                contentDescription = null,
                modifier = Modifier.padding(10.dp),
                contentScale = ContentScale.Fit
            )
        }

        // 2. Product Details
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF182B3C),
                maxLines = 1
            )
            Text(
                text = item.category.uppercase(),
                fontSize = 11.sp,
                color = Color.LightGray,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "Rs. ",
                    fontSize = 14.sp,
                    color = Color(0xFF2ABB00),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${item.price * item.quantity}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2ABB00)
                )
            }
        }
    }
}

@Composable
fun PaymentItem(icon: Int, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            alpha = if (isSelected) 1f else 0.5f
        )
        Text(
            text = label,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.Black else Color.Gray
        )
        if (isSelected) {
            Icon(
                painter = painterResource(id = R.drawable.tick),
                contentDescription = null,
                tint = Color(0xFF2ABB00),
                modifier = Modifier.size(20.dp)
            )

        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_front),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun GrandTotal(price: String) {
    // 1. Outer Box that holds everything and allows overflow
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp), // Height of bar (80) + half the circle (20)
        contentAlignment = Alignment.BottomCenter
    ) {
        // 2. The White Bar (Surface) - Exactly 80dp high
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Grand Total", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = " *included TAX",
                            fontSize = 10.sp,
                            color = Color.LightGray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                Text(
                    text = "Rs. $price",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2ABB00)
                )
            }
        }

        // 3. The Circle - Placed OUTSIDE the Surface so it doesn't get clipped
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(40.dp)
                .background(Color(0xFF2ABB00), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(90f)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    MaterialTheme {
        CheckoutScreen(
            viewModel = CheckoutViewModel(),
            onBackClick = {}
        )
    }
}
