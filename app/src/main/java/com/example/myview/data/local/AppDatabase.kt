package com.example.myview.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CartEntity::class,
                FavoriteEntity::class
    ],

    version = 2, // as there are two tables in the same database now(ie:Room database schema version 2)
    // as in AppDatabase has two tables 1.cart 2.Favorites
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao


}