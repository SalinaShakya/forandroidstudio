package com.example.myview.ui.viewmodel
import androidx.lifecycle.ViewModel
//import androidx.preference.isNotEmpty
import com.example.myview.data.CartManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.location.Geocoder
import java.util.Locale
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CheckoutViewModel : ViewModel(){


    // 1. Get items from CartManager
    val cartItems = CartManager.cartItems

    // 2. Track user selections
    private val _selectedPayment = MutableStateFlow("Cash on Delivery")
    val selectedPayment: StateFlow<String> = _selectedPayment

    private val _deliveryAddress = MutableStateFlow("Delivery Address Not Set")
    val deliveryAddress: StateFlow<String> = _deliveryAddress

    init {
        loadAddressFromFirestore()
    }

    //TO SAVE DATA (ADDRESS) IN THE FIRESTORE
    private fun loadAddressFromFirestore() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("Users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val savedAddress = document.getString("address")
                        if (!savedAddress.isNullOrEmpty()) {
                            _deliveryAddress.value = savedAddress
                        }
                    }
                }
        }
    }

    // 3. Calculation logic
    fun getGrandTotal(): Double {
        return cartItems.sumOf { it.price * it.quantity }
    }

    // 4. Actions
    fun selectPayment(method: String) {
        _selectedPayment.value = method
    }

//    fun updateAddress(lat: Double, lon: Double) {
//        _deliveryAddress.value = "Lat: ${String.format("%.4f", lat)}, Lon: ${String.format("%.4f", lon)}"
//
//    }
fun updateAddress(context: android.content.Context, lat: Double, lon: Double) {
    val geocoder = android.location.Geocoder(context, java.util.Locale.getDefault())
    try {
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        val readableAddress = if (addresses?.isNotEmpty() == true) {
            addresses[0].getAddressLine(0)
        } else {
            "Lat: $lat, Lon: $lon"
        }

        // Update UI
        _deliveryAddress.value = readableAddress

        // Sync to Firebase
        saveAddressToFirestore(readableAddress)

    } catch (e: Exception) {
        _deliveryAddress.value = "Lat: $lat, Lon: $lon"
    }
}

private fun saveAddressToFirestore(address: String) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    if (uid != null) {
        FirebaseFirestore.getInstance().collection("Users").document(uid)
            .update("address", address)
            .addOnFailureListener {
                // If document update fails (e.g. field doesn't exist), try setting it
                FirebaseFirestore.getInstance().collection("Users").document(uid)
                    .set(mapOf("address" to address), com.google.firebase.firestore.SetOptions.merge())
            }
    }
}


    fun placeOrder(onComplete: () -> Unit) {
        // Logic to sync with Firestore goes here...
        onComplete()
    }
}
