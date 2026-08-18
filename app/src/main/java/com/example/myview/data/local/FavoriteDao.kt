package com.example.myview.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao

    interface FavoriteDao {

        @Query("SELECT * FROM favorites")
        suspend fun getAllFavorites(): List<FavoriteEntity>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertFavorite(favorite: FavoriteEntity)

        @Delete
        suspend fun deleteFavorite(favorite: FavoriteEntity)

        @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
        suspend fun isFavorite(id: Int): Boolean
    }
