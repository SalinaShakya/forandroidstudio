package com.example.myview.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.example.myview.data.local.DatabaseProvider
import com.example.myview.data.local.FavoriteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object FavoriteManager {

    val favorites = mutableStateListOf<FavoriteEntity>()

    private lateinit var favoriteDao: com.example.myview.data.local.FavoriteDao

    fun init(context: Context, onLoaded: (() -> Unit)? = null) {

        favoriteDao = DatabaseProvider
            .getDatabase(context)
            .favoriteDao()

        CoroutineScope(Dispatchers.IO).launch {

            val savedFavorites = favoriteDao.getAllFavorites()

            withContext(Dispatchers.Main) {
                favorites.clear()
                favorites.addAll(savedFavorites)

                onLoaded?.invoke()
            }
        }
    }

    fun addFavorite(favorite: FavoriteEntity) {

        favorites.add(favorite)

        CoroutineScope(Dispatchers.IO).launch {
            favoriteDao.insertFavorite(favorite)
        }
    }

    fun removeFavorite(favorite: FavoriteEntity) {

        favorites.removeAll { it.id == favorite.id }

        CoroutineScope(Dispatchers.IO).launch {
            favoriteDao.deleteFavorite(favorite)
        }
    }

    fun isFavorite(
        id: Int,
        onResult: (Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {

            val result = favoriteDao.isFavorite(id)

            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
}