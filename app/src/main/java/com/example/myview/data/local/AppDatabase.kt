package com.example.myview.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CartEntity::class,
        FavoriteEntity::class,
        ShippingAddressEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun shippingAddressDao(): ShippingAddressDao

}
