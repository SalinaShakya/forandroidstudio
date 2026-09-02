package com.example.myview.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.example.myview.data.local.DatabaseProvider
import com.example.myview.data.local.FavoriteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



//1stscreen-manager-dao-room
//dao talks to the database so dao.insertfav=dao handles the oepration
object FavoriteManager { //single instance as in no need to val fav=FavoriteManger,.add

    val favorites = mutableStateListOf<FavoriteEntity>() //favoriteEntity compose ui
    //the COMPOSE can notice list change and update ui
    val favoritesObservable = androidx.lifecycle.MutableLiveData<List<FavoriteEntity>>() //for realtime update //xml
    private fun notifyChange() {
        favoritesObservable.postValue(favorites)
    }
    private lateinit var favoriteDao: com.example.myview.data.local.FavoriteDao

    fun init(context: Context, onLoaded: (() -> Unit)? = null) {
        //imp as in initialize the FavoriteManager and load the favs to the database

        favoriteDao = DatabaseProvider
            .getDatabase(context)
            .favoriteDao()
        //get the database and gets the fav dao

        CoroutineScope(Dispatchers.IO).launch {
            //simply moves to the io thread instead of the main as the database operations must block the main thread

            val savedFavorites = favoriteDao.getAllFavorites()
            //retrieves the saved favs from the database(the cart has it)

            withContext(Dispatchers.Main) { //switch to the main thread
                favorites.clear()
                favorites.addAll(savedFavorites)
//replaces the old favs with the new ones from room
                //room-savedfav-fav-ui
                onLoaded?.invoke()
            }
        }
    }

    fun addFavorite(favorite: FavoriteEntity) {

//        favorites.add(favorite)
//        CoroutineScope(Dispatchers.IO).launch {
//            favoriteDao.insertFavorite(favorite)
        val exists = favorites.any { it.id == favorite.id }

                if (!exists) {
                    //  ONLY add if it's not already there
                    favorites.add(favorite)

                    CoroutineScope(Dispatchers.IO).launch {
                        favoriteDao.insertFavorite(favorite)
                        notifyChange()
                    }
                }

        }

    fun removeFavorite(favorite: FavoriteEntity) {

        favorites.removeAll { it.id == favorite.id }

        CoroutineScope(Dispatchers.IO).launch {
            favoriteDao.deleteFavorite(favorite)
            notifyChange()
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