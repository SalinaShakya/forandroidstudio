package com.example.myview.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myview.data.CartManager
import com.example.myview.data.local.DatabaseProvider
import com.example.myview.data.local.ShippingAddressEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {

    private val addressDao = DatabaseProvider.getDatabase(CartManager.context!!).shippingAddressDao()

    // 1. Get items from CartManager
    val cartItems = CartManager.cartItems

    // 2. Track user selections
    private val _selectedPayment = MutableStateFlow("Cash on Delivery")
    val selectedPayment: StateFlow<String> = _selectedPayment

    private val _deliveryAddress = MutableStateFlow("Delivery Address Not Set")
    val deliveryAddress: StateFlow<String> = _deliveryAddress


    val subTotal: Double get() = cartItems.sumOf { it.price * it.quantity }
    val taxAmount: Double get() = subTotal * 0.13 // 13% Tax
    val shippingCharge: Double = 1.0 // Fixed Rs. 100

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    // NEW: Support for multiple addresses observed from Room
    val addressList: StateFlow<List<ShippingAddressEntity>> = addressDao.getAllAddresses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addAddress(name: String, phone: String, address: String, label: String = "Home") {
        val id = java.util.UUID.randomUUID().toString()
        val entity = ShippingAddressEntity(
            id = id,
            fullName = name,
            phone = phone,
            address = address,
            label = label,
            isDefault = false
        )
        viewModelScope.launch(Dispatchers.IO) {
            addressDao.insertAddress(entity)
        }
        
        // Auto-select if it's the first one
        if (_deliveryAddress.value == "Delivery Address Not Set") {
            _fullName.value = name
            _deliveryAddress.value = address
        }
    }

    fun selectAddress(shippingAddress: ShippingAddressEntity) {
        _fullName.value = shippingAddress.fullName
        _deliveryAddress.value = shippingAddress.address
        // Sync to cloud if needed
        saveAddressToFirestore(shippingAddress.address)
    }

    fun deleteAddress(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addressDao.deleteById(id)
        }
    }

    init {
        loadAddressFromFirestore()
    }

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
    fun getGrandTotal(): Double = subTotal + taxAmount + shippingCharge


    // 4. Actions
    fun selectPayment(method: String) {
        _selectedPayment.value = method
    }

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

    fun setAddress(name: String, phone: String, newAddress: String) {
        _fullName.value = name
        _deliveryAddress.value = newAddress
        saveAddressToFirestore(newAddress)
    }

    private fun saveAddressToFirestore(address: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("Users").document(uid)
                .update("address", address)
                .addOnFailureListener {
                    FirebaseFirestore.getInstance().collection("Users").document(uid)
                        .set(mapOf("address" to address), com.google.firebase.firestore.SetOptions.merge())
                }
        }
    }

    fun placeOrder(onComplete: () -> Unit) {
        onComplete()
    }
}
