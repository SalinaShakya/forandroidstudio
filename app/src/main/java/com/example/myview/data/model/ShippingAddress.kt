package com.example.myview.data.model

data class ShippingAddress(
    val id: String = java.util.UUID.randomUUID().toString(),
    val fullName: String,
    val phone: String,
    val address: String,
    val label: String = "Home",
    val isDefault: Boolean = false
)
