package com.example.myview.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.myview.data.CartManager
import com.example.myview.data.model.CartItem


//so what the ViewModel actually does is the math watches the cartItems list
//and whenever the changes occur the it does the sumOf fun
//and puts the result into a liveData box(totalPrice ig)

class CartViewModel : ViewModel() {

    // 1. Observe the items from the CartManager
    // This uses the LiveData you already have in CartManager.kt
    val cartItems: LiveData<List<CartItem>> = CartManager.cartObservable

    // 2. Calculated state for the UI
    // The 'map' function ensures these update automatically when items change
    val totalPrice: LiveData<Double> = cartItems.map { items ->
        items.sumOf { it.price * it.quantity }
    }

    val itemCount: LiveData<Int> = cartItems.map { items ->
        items.sumOf { it.quantity }
    }

    // 3. Actions (Plus / Minus / Remove)
    // These functions talk to the CartManager to update the database
    fun incrementQuantity(item: CartItem) {
        CartManager.updateQuantity(item.id, item.quantity + 1)
    }

    fun decrementQuantity(item: CartItem) {
        if (item.quantity > 1) {
            CartManager.updateQuantity(item.id, item.quantity - 1)
        } else {
            CartManager.removeFromCart(item.id)
        }
    }

    fun removeItem(item: CartItem) {
        CartManager.removeFromCart(item.id)
    }
}