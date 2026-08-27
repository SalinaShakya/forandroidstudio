package com.example.myview.ui.viewmodel
import androidx.lifecycle.ViewModel
import com.example.myview.data.CartManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CheckoutViewModel : ViewModel(){


    // 1. Get items from CartManager
    val cartItems = CartManager.cartItems

    // 2. Track user selections
    private val _selectedPayment = MutableStateFlow("Cash on Delivery")
    val selectedPayment: StateFlow<String> = _selectedPayment

    // 3. Calculation logic
    fun getGrandTotal(): Double {
        return cartItems.sumOf { it.price * it.quantity }
    }

    // 4. Actions
    fun selectPayment(method: String) {
        _selectedPayment.value = method
    }

    fun placeOrder(onComplete: () -> Unit) {
        // Logic to sync with Firestore goes here...
        onComplete()
    }
}
