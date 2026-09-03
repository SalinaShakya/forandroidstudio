package com.example.myview.fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Heading
            Text(
                text = "Details for shipping",
                style = MaterialTheme.typography.titleMedium
            )

            // Full Name
            Text(
                text = "Full Name"
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = {
                    Text("Enter Full Name")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Mobile Number
            Text(
                text = "Mobile Number"
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = {
                    Text("Enter mobile No.")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Address
            Text(
                text = "Address"
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = {
                    Text("Enter Address")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 1
            )

            // Label
            Text(
                text = "Select a label"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = { }
                ) {
                    Text("Home")
                }

                Button(
                    onClick = { }
                ) {
                    Text("Office")
                }

                Button(
                    onClick = { }
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

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // Save button
            Button(
                onClick = {
                    onSave(
                        fullName,
                        phone,
                        address
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("SAVE")
            }
        }
    }
}


// -------------------------
// CUSTOM SWITCH
// -------------------------

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