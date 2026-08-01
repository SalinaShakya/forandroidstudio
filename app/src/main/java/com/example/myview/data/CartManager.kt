package com.example.myview.data

import com.example.myview.data.model.CartItem
import com.example.myview.data.model.ProductResponse

object CartManager {

    val cartItems = mutableListOf<CartItem>()

    fun addToCart(product: ProductResponse) {

        val existing = cartItems.find { it.id == product.id }

        if (existing != null) {

            existing.quantity++

        } else {

            cartItems.add(
                CartItem(
                    id = product.id,
                    title = product.title,
                    price = product.price,
                    image = product.image,
                    quantity = 1
                )
            )
        }
    }

    fun updateQuantity(id: Int, quantity: Int) {

        cartItems.find { it.id == id }?.quantity = quantity

    }

    fun removeFromCart(id: Int) {

        cartItems.removeAll { it.id == id }

    }
}