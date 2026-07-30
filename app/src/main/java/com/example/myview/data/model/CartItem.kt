package com.example.myview.data.model

data class CartItem(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    var quantity: Int
)
