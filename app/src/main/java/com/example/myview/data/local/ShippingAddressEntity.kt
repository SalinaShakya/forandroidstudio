package com.example.myview.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shipping_addresses")
data class ShippingAddressEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val fullName: String,
    val phone: String = "",
    val address: String,
    val label: String = "Home",
    val isDefault: Boolean = false
)
