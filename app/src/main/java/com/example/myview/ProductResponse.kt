package com.example.myview

// Matches the exact JSON structure returned from https://fakestoreapi.com
data class ProductResponse(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String, // String URL pointing directly to the product photo
    val rating: Rating
)

data class Rating(
    val rate: Double,
    val count: Int
)
