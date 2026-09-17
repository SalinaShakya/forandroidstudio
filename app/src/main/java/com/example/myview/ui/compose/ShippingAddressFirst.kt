package com.example.myview.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.myview.R
import com.example.myview.data.local.ShippingAddressEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingAddressFirst(
    onBack: () -> Unit = {},
    onEmpty: () -> Unit = {},
    onAddNew: () -> Unit = {},
    onEdit: (com.example.myview.data.local.ShippingAddressEntity) -> Unit = {},
    viewModel: com.example.myview.ui.viewmodel.CheckoutViewModel = viewModel()
) {
    val addresses by viewModel.addressList.collectAsState()

    // TRIGGER: Redirect when empty list
    LaunchedEffect(addresses) {
        // Room needs a split second to load from disk. 
        // We wait 300ms before deciding if the list is truly empty.
        delay(300)
        if (addresses.isEmpty()) {
            onEmpty()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        CenterAlignedTopAppBar(
            title = { Text("Shipping Address") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = addresses,
                key = { it.id }
            ) { item ->
                val scope = rememberCoroutineScope()
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { false } // Keep the box open for manual interaction
                )

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = false,
                    enableDismissFromEndToStart = true,
                    backgroundContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFF2F2F5), RoundedCornerShape(18.dp))
                                .padding(end = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF2ABB00), CircleShape)
                                        .padding(8.dp)
                                        .clickable {
                                            onEdit(item)
                                            scope.launch { dismissState.snapTo(SwipeToDismissBoxValue.Settled) }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_edit),
                                        contentDescription = "Edit",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Color.Red, CircleShape)
                                        .padding(8.dp)
                                        .clickable {
                                            viewModel.deleteAddress(item.id)
                                            scope.launch { dismissState.snapTo(SwipeToDismissBoxValue.Settled) }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_trash),
                                        contentDescription = "Delete",
                                        tint = Color.White
                                    )
                                }

                            }
                        }
                    },
                    content = {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .clickable {
                                    viewModel.selectAddress(item)
                                    onBack()
                                },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize().padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(80.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFF2F2F2)
                                ) {
                                    @OptIn(ExperimentalGlideComposeApi::class)
                                    GlideImage(
                                        model = R.drawable.location,
                                        contentDescription = null,
                                        modifier = Modifier.padding(8.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }

                                Column(
                                    modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
                                ) {
                                    Text(text = item.fullName, style = MaterialTheme.typography.titleSmall)
                                    Text(text = item.address, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = item.label, color = Color.Gray)
                                }
                            }
                        }
                    }
                )
            }

            item {
                Button(
                    onClick = onAddNew,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ABB00)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ADD NEW ADDRESS", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ShippingAddressFirstPreview() {
    MaterialTheme {
        ShippingAddressFirst()
    }
}