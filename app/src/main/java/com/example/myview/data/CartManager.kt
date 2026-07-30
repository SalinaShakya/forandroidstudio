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
    fun removeFromCart(productId: Int) {
        cartItems.removeAll { it.id == productId }
    }

    fun updateQuantity(productId: Int, quantity: Int) {
        val item = cartItems.find { it.id == productId }
        item?.quantity = quantity
    }
}