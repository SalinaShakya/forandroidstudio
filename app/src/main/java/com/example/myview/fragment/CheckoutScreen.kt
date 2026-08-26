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
import com.example.myview.R
import com.example.myview.data.CartManager
import com.example.myview.data.local.FavoriteEntity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.myview.data.model.CartItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit = {}
) {
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
            GrandTotal(price = "19,500.00")
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
                        Image(painter = painterResource(id = R.drawable.ic_edit), contentDescription = null, modifier = Modifier
                            .size(38.dp)
                            .offset(x = 270.dp))
                    }
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(text = "Delivery Address", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF808080))
                        Text(text = "Pulchowk, Lalitpur-20", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF182B3C))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Order Summary", fontSize = 15.sp, modifier = Modifier.padding(start = 4.dp))
            Spacer(modifier = Modifier.height(16.dp))

            // Order Summary Box
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    CartManager.cartItems.forEach { item ->
                        OrderItemRow(item)
                        if (item != CartManager.cartItems.last()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                thickness = 0.5.dp,
                                color = Color.LightGray.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Promo Code Section
//            Box(modifier = Modifier.width(200.dp).height(50.dp), contentAlignment = Alignment.CenterStart) {
//                Image(painter = painterResource(id = R.drawable.rect_clear), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
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
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp)
                ) {
                    PaymentItem(icon = R.drawable.ic_payment, label = "Cash on Delivery")
                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    PaymentItem(icon = R.drawable.ic_brand, label = "Pay with eSewa")
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
                text = "BRAND NAME", // Placeholder for subtitle
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
fun PaymentItem(icon: Int, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(28.dp))
        Text(text = label, modifier = Modifier
            .padding(start = 16.dp)
            .weight(1f), fontSize = 14.sp)
        Image(painter = painterResource(id = R.drawable.ic_front), contentDescription = null, modifier = Modifier.size(16.dp))
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
            modifier = Modifier.fillMaxWidth().height(80.dp),
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
                modifier = Modifier.size(20.dp).rotate(90f)
            )
        }
    }
}


            @Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    MaterialTheme {
        CheckoutScreen()

    }
}
