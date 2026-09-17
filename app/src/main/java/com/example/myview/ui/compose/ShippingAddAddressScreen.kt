package com.example.myview.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myview.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingAddAddressScreen(
    onBackClick: () -> Unit = {},
    addadressnow: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(

                title = {
                    Text(
                        text = "Shipping Address",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
//


                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {


                Spacer(modifier = Modifier.height(40.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_setlocation),
                            contentDescription = "No address added yet!"
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "No address added yet!",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "You have not added any shipping address yet.",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = addadressnow,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50), // This is the Green color used elsewhere in your app
                                contentColor = Color.White        // This is the text color
                            ),
                            shape = RoundedCornerShape(8.dp) // Optional: add rounding to match your app style
                        ) {
                            Text("ADD ADDRESS NOW")
                        }

                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ShippingAddAddressScreenPreview() {
    MaterialTheme {
        val item=0
        ShippingAddAddressScreen()
    }
}