package com.example.myview.fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Surface
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
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
    // 1. Selection State
    val selectedItems = remember { mutableStateListOf<Int>() }
    val isAllSelected = items.isNotEmpty() && selectedItems.size == items.size

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

                //cart
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
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn( // the recycler view as in creating everything at once it only creates on whats visible
            modifier = Modifier
                .fillMaxSize() //take up all the available space of the parent(width and height)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 0.dp) //for that toolbar going down (adjust)
        ) {
            item { // The Header with Select All
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isAllSelected,
                            onCheckedChange = { checked ->
                                selectedItems.clear()
                                if (checked) {
                                    selectedItems.addAll(items.map { it.id })
                                }
                            },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50))
                        )
                        Text(
                            text = "Items (${items.size})",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(onClick = { /* Handle delete all logic */ }) {
                        Text(
                            text = "DELETE ALL",
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            items( // this is the loop thing in which the fav card is made for each fav entity
                items = items,
                key = { it.id } // Adding a key helps Compose keep track of items during swipe(unique key) as compose might mix cards up
                // every card is connected to the phone ig
            ) { item -> // as in inside every item
                val isSelected = selectedItems.contains(item.id)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 8.dp) // Extra top padding to allow overlap
                ) {
                    // 1. The Dismissible Card
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                FavoriteManager.removeFavorite(item)
                            }
                            true
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true,
                        modifier = Modifier.fillMaxWidth(),
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFF2F2F5), RoundedCornerShape(18.dp))
                                    .padding(end = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color.Red, CircleShape)
                                        .padding(8.dp),
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
                    ) {
                        // Wrapping Card and Circle so they move together
                        Box {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Product Image Box
                                    Surface(
                                        modifier = Modifier.size(80.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFFF2F2F2)
                                    ) {
                                        @OptIn(ExperimentalGlideComposeApi::class)
                                        GlideImage(
                                            model = item.image,
                                            contentDescription = null,
                                            modifier = Modifier.padding(8.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    }

                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 12.dp)
                                    ) {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = "CELEINE", // Placeholder brand
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.LightGray
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Rs. ${item.price}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4CAF50)
                                        )
                                    }

                                    Column(
                                        modifier = Modifier.fillMaxHeight(),
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = null,
                                            tint = Color.Black
                                        )

                                        Surface(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable { /* Add to cart */ },
                                            color = Color(0xFF4CAF50)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.checkout_cart),
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 2. Selection Circle (Now inside SwipeToDismissBox content, so it moves!)
                            Box(
                                modifier = Modifier
                                    .offset(x = (-8).dp, y = (-8).dp)
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Color(0xFF4CAF50) else Color(0xFFE0E0E0))
                                    .clickable {
                                        if (isSelected) selectedItems.remove(item.id)
                                        else selectedItems.add(item.id)
                                    }
                                    .align(Alignment.TopStart),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
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
