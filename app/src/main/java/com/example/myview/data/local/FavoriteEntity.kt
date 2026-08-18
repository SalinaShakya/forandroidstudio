package com.example.myview.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")

data class FavoriteEntity(
@PrimaryKey
val id: Int,
val title: String,
val price: Double,
val image: String
)
