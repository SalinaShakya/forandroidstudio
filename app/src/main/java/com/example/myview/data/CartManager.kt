package com.example.myview.data

import android.R.attr.category
import android.content.Context
import com.example.myview.data.model.CartItem
import com.example.myview.data.model.ProductResponse
import com.example.myview.data.local.DatabaseProvider
import com.example.myview.data.local.CartEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object CartManager {

    val cartItems = mutableListOf<CartItem>() //cartitem
    private lateinit var cartDao: com.example.myview.data.local.CartDao

    val cartObservable = androidx.lifecycle.MutableLiveData<List<CartItem>>() //for my updates realtime
    //    fun init(context: Context) {

    private fun notifyChange() {
        cartObservable.postValue(cartItems)
    }
    fun init(context: Context, onLoaded: (() -> Unit)? = null) {

        cartDao = DatabaseProvider
            .getDatabase(context)
            .cartDao()

        CoroutineScope(Dispatchers.IO).launch {
//  (inside it everything is on a background thread)
            val savedItems = cartDao.getAllCartItems()

//            cartItems.clear()

//            cartItems.addAll(
//                savedItems.map { entity ->
//                    CartItem(
//                        id = entity.id,
//                        title = entity.title,
//                        price = entity.price,
//                        image = entity.image,
//                        quantity = entity.quantity
//                    )
//                }
//            )( The Crash Scenario) If the user opens the app and immediately goes to the Cart screen while this background thread is still running, the RecyclerView (which is on the Main thread) will try to read the list at the same exact microsecond the Background thread is trying to clear it.
            //example:If the user is currently scrolling the list, and the chef tries to clear or add items at the same time, Android panics and crashes the app.

            val items = savedItems.map { entity ->
                CartItem(
                    id = entity.id,
                    title = entity.title,
                    price = entity.price,
                    image = entity.image,
                    quantity = entity.quantity,
                    category=entity.category
                )
            }
            //only one person (Main Thread) is touching the (cartItems) at a time (waiter,cookie jar)
            //update the ui list
            withContext(Dispatchers.Main) {
                cartItems.clear()
                cartItems.addAll(items)
                notifyChange()

                // 4. Callback is also safe now because we are on Main thread(listening screens that the data is ready)
                onLoaded?.invoke()
            }


//            CoroutineScope(Dispatchers.Main).launch {
//                onLoaded?.invoke()
//            }
        }
    }

    fun addToCart(product: ProductResponse) {

        val existing = cartItems.find { it.id == product.id }

        if (existing != null) {

            existing.quantity++
            updateInRoomAndCloud(existing) //for cloud
//            CoroutineScope(Dispatchers.IO).launch {
//
//                cartDao.updateCartItem(
//                    CartEntity(
//                        id = existing.id,
//                        title = existing.title,
//                        price = existing.price,
//                        image = existing.image,
//                        quantity = existing.quantity
//                    )
//                )
//            }
            notifyChange()

        } else {

            val item = CartItem(
                id = product.id,
                title = product.title,
                price = product.price,
                image = product.image,
                quantity = 1,
                category = product.category
            )

            cartItems.add(item)

//            CoroutineScope(Dispatchers.IO).launch {
//
//                cartDao.insertCartItem(
//                    CartEntity(
//                        id = item.id,
//                        title = item.title,
//                        price = item.price,
//                        image = item.image,
//                        quantity = item.quantity
//                    )
//                )
//            }
            insertInRoomAndCloud(item)
        }
    }


    fun updateQuantity(id: Int, quantity: Int) {

//        cartItems.find { it.id == id }?.quantity = quantity
        cartItems.find { it.id == id }?.let { item ->

            item.quantity = quantity
//
//            CoroutineScope(Dispatchers.IO).launch {
//
//                cartDao.updateCartItem(
//                    CartEntity(
//                        id = item.id,
//                        title = item.title,
//                        price = item.price,
//                        image = item.image,
//                        quantity = item.quantity
//                    )
//                )
//            }
            updateInRoomAndCloud(item)
            notifyChange()
        }
    }


    fun removeFromCart(id: Int) {

//        cartItems.removeAll { it.id == id }
        cartItems.find { it.id == id }?.let { item ->

            cartItems.remove(item)

//            CoroutineScope(Dispatchers.IO).launch {
//
//                cartDao.deleteCartItem(
//                    CartEntity(
//                        id = item.id,
//                        title = item.title,
//                        price = item.price,
//                        image = item.image,
//                        quantity = item.quantity
//                    )
//                )
//            }
            deleteFromRoomAndCloud(item)
            notifyChange()
        }
    }

    private fun insertInRoomAndCloud(item: CartItem) {
        // 1. Save to local Room
        CoroutineScope(Dispatchers.IO).launch {
            cartDao.insertCartItem(
                CartEntity(
                    item.id,
                    item.title,
                    item.price,
                    item.image,
                    item.quantity,
                    item.category
                )
            )
        }
        // 2. Sync to Firestore (if logged in)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val cloudData = hashMapOf(
                "productId" to item.id.toString(),
                "title" to item.title,
                "priceAtAddition" to item.price,
                "image" to item.image,
                "quantity" to item.quantity,
                "category" to item.category,
                "updatedAt" to Timestamp.now()
            )
            FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("Cart").document(item.id.toString()).set(cloudData)
        }
    }

    private fun updateInRoomAndCloud(item: CartItem) {
        CoroutineScope(Dispatchers.IO).launch {
            cartDao.updateCartItem(
                CartEntity(
                    item.id,
                    item.title,
                    item.price,
                    item.image,
                    item.quantity,
                    item.category
                )
            )
        }
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("Cart").document(item.id.toString())
                .update("quantity", item.quantity, "updatedAt", Timestamp.now())
        }
    }

    private fun deleteFromRoomAndCloud(item: CartItem) {
        CoroutineScope(Dispatchers.IO).launch {
            cartDao.deleteCartItem(
                CartEntity(
                    item.id,
                    item.title,
                    item.price,
                    item.image,
                    item.quantity,
                    item.category
                )
            )
        }
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("Cart").document(item.id.toString()).delete()
        }
    }
    fun syncFromCloud(onComplete: () -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // 1. Fetch from Firestore
        FirebaseFirestore.getInstance().collection("Users").document(uid)
            .collection("Cart").get()
            .addOnSuccessListener { snapshot ->
                val cloudItems = mutableListOf<CartItem>()

                for (doc in snapshot.documents) {
                    val id = doc.getString("productId")?.toInt() ?: 0
                    val title = doc.getString("title") ?: ""
                    val price = doc.getDouble("priceAtAddition") ?: 0.0
                    val image = doc.getString("image") ?: ""
                    val qty = doc.getLong("quantity")?.toInt() ?: 1
                    val cat = doc.getString("category") ?: "Uncategorized" //get from cloud
                    cloudItems.add(CartItem(id, title, price, image, qty,cat)) //where is this coming from
                }
                // 2. Save to Room & Update local list
                CoroutineScope(Dispatchers.IO).launch {
                    cartDao.clearCart() // Wipe old guest items
                    cloudItems.forEach { item ->
                        cartDao.insertCartItem(CartEntity(item.id, item.title, item.price, item.image, item.quantity,item.category))
                    }

                    withContext(Dispatchers.Main) {
                        cartItems.clear()
                        cartItems.addAll(cloudItems)
                        notifyChange()
                        onComplete() // Tell the UI to refresh
                    }
                }
            }
    }
}