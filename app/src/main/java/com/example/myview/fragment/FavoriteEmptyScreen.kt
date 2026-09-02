package com.example.myview.fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myview.R
import com.example.myview.data.local.FavoriteEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteEmptyScreen(
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onContinueShopping: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Text(
                        text = "Favorites",
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
//                actions = {
//                    IconButton(onClick = onCartClick) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.forcartig),
//                            contentDescription = "Cart",
//                            tint = Color.Unspecified
//                        )
//                    }
//                } that made it circle
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp) // Space from the right edge
                            .size(40.dp) // Define the square size
                            .clip(RectangleShape) // Ensures it's a rectangle
                            .clickable { onCartClick() }, // Makes the whole rectangle clickable
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.forcartig),
                            contentDescription = "Cart",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(44.dp) // Size of the icon inside the box
                        )
                    }
                }
               ,
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
                Text(
                    text = "Items (0)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

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
                            painter = painterResource(id = R.drawable.empty_favorite),
                            contentDescription = "Empty Favorites"
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "No favorites yet.",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Add your favorites to your wishlist and they will show here.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray


                        )

                        Spacer(modifier = Modifier.height(24.dp))

//                    Button(onClick = onContinueShopping) {
//                        Text("CONTINUE SHOPPING")
//                    }
                        Button(
                            onClick = onContinueShopping,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50), // This is the Green color used elsewhere in your app
                                contentColor = Color.White        // This is the text color
                            ),
                            shape = RoundedCornerShape(8.dp) // Optional: add rounding to match your app style
                        ) {
                            Text("CONTINUE SHOPPING")
                        }

                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun FavoriteEmptyScreenPreview() {
    MaterialTheme {
       val item=0
        FavoritesScreen(items = emptyList())
    }
}