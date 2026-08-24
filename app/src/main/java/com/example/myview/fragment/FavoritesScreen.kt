package com.example.myview.fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myview.R

//for the recycler
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.myview.data.FavoriteManager
import com.example.myview.data.local.FavoriteEntity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen( //the main
    items: List<FavoriteEntity>,
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    Scaffold( // the page container
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White, // for padding bottom
        topBar = {
            CenterAlignedTopAppBar( // creates the topbar(toolbar)
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Text(
                        text = "Favorites",
                        style = MaterialTheme.typography.titleMedium
                    )
                },

                // LEFT: Back arrow
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                // RIGHT: Cart
                actions = {
                    IconButton(
                        onClick = onCartClick
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.forcartig),
                            //painterResource simply loads an image from my drawable
                            contentDescription = "Cart",
                            tint = Color.Unspecified
                        )
                    }
                },

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn( // the recycler view as in creating everything at once it only creates on whats visible
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 0.dp) //for that toolbar going down (adjust)
        ) {
            item { //ig this is called the header
                Text(
                    text = "Items (${items.size})",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items( // this is the loop thing in which the fav card is made for each fav entity
                items = items,
                key = { it.id } // Adding a key helps Compose keep track of items during swipe(unique key) as compose might mix cards up
                // every card is connected to the phone ig
            ) { item -> // as in inside every item
                val dismissState = rememberSwipeToDismissBoxState( // this remembers how far the card has been swiped
                    // not moved is default, halfway its sliding and swiped from right to left then its deleted(endToStart)
                    confirmValueChange = { value ->
                        if (value == SwipeToDismissBoxValue.EndToStart) { // ya so if the state above is at the endTostart then (as in we swiped right to left)
                            FavoriteManager.removeFavorite(item) // we delete this
                        }
                        true
                    }
                )

                SwipeToDismissBox( // this is for the gmail style as in the full swipe to delete
                    state = dismissState,
                    enableDismissFromStartToEnd = false,
                    enableDismissFromEndToStart = true,
                    backgroundContent = { // whats happening when swiped
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp)
                                .background(Color(0xFFF2F2F5), RoundedCornerShape(18.dp))
                                .padding(end = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color.Red, CircleShape)
                                    .padding(8.dp), // Controls the size of the red circle around the trash can
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
//                                imageVector = Icons.Default.Delete,
                                    painter = painterResource(id = R.drawable.ic_trash),
                                    contentDescription = "Delete",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            @OptIn(ExperimentalGlideComposeApi::class)
                            GlideImage(
                                model = item.image,
                                contentDescription = "Product Image",
                                modifier = Modifier
                                    .size(110.dp)
                                    .padding(start = 12.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            // 2. Product Details
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 12.dp)
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "IN STOCK - 99SHOP",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Rs. ${item.price}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                            }

                            Icon(
                                painter = painterResource(id = R.drawable.group),
                                contentDescription = "Favorite",
                                tint = Color.Black,
                                modifier = Modifier.offset(x = (-20).dp, y = (-45).dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    MaterialTheme {
        val sampleItems = listOf(
            FavoriteEntity(
                id = 1,
                title = "Sample Product",
                price = 999.0,
                image = ""
            )
        )
        FavoritesScreen(items = sampleItems)
    }
}
