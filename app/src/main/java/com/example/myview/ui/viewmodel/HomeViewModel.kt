package com.example.myview.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myview.data.CartManager
import com.example.myview.data.api.RetrofitClient
import com.example.myview.data.model.Popular
import com.example.myview.data.model.ProductResponse
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel (){

// 1. Added ": ViewModel()" so it survives rotations

    private val _featuredProducts = MutableLiveData<List<ProductResponse>>()
    val featuredProducts: LiveData<List<ProductResponse>> = _featuredProducts

    private val _hotDeals = MutableLiveData<List<ProductResponse>>()
    val hotDeals: LiveData<List<ProductResponse>> = _hotDeals

    private val _popularCategories = MutableLiveData<List<Popular>>()
    val popularCategories: LiveData<List<Popular>> = _popularCategories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _popularBrands = MutableLiveData<List<ProductResponse>>()
    val popularBrands: LiveData<List<ProductResponse>> = _popularBrands

    private val _recommendedItems = MutableLiveData<List<ProductResponse>>()
    val recommendedItems: LiveData<List<ProductResponse>> = _recommendedItems


    // 2. Added an error LiveData (Professional practice)
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage


    fun fetchHomeData() {

        // THE GUARD: If we already have products, just sync the numbers and STOP.
        if (_featuredProducts.value != null && _featuredProducts.value!!.isNotEmpty()) {
            refreshQuantitiesOnly() // Make sure cart numbers are right
            return // Exit the function here; do NOT hit the internet
//            Log.d("SYNC_TEST", "GUARD HIT: Skipping Internet, just syncing local numbers.")
//            refreshQuantitiesOnly()
//            return
        }
        Log.d("SYNC_TEST", "FIRST LOAD: Going to the Internet...")
        //if its the first time you fetch the data
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // 1. Fetch all data in the background
                //as using a variable vval at the start, the carousel wont appear until the entire list of products is also ready
                val products = RetrofitClient.apiService.getFeaturedProducts()
                val deals = RetrofitClient.apiService.getHotDeals()
                val categories = RetrofitClient.apiService.getPopularCategories()

// DATA LEAKING TO THE UI (BAD) as soon as getHotDeals() finished it updated the liveData, then fragments observer heard this and updated the carousel and the user saw carousel appear even when the rest of the page was still downloading the other products and running the sync loop
// bad ux (shows lagging, quantity fluctuation, carousel only on display,pushing the carousel down
//                _hotDeals.value = RetrofitClient.apiService.getHotDeals()
//                _popularCategories.value = RetrofitClient.apiService.getPopularCategories()

                // 2. Sync Loop: Check if any products are already in the cart
                products.forEach { apiProduct ->
                    val cartItem = CartManager.cartItems.find { it.id == apiProduct.id }
                    if (cartItem != null) {
                        apiProduct.quantity = cartItem.quantity
                    }
                }

                // 3. Distribute the synced data to specific lists (update all LiveData at the same time
                //refresh of them in a single frame
                _hotDeals.value = deals
                _popularCategories.value = categories
                _featuredProducts.value = products.take(4)          // First 4 items
                _popularBrands.value = products.drop(4).take(4)      // Next 4 items
                _recommendedItems.value = products.drop(8)           // The rest

                // 4. Success: Stop the loading spinner
                _isLoading.value = false

            } catch (e: Exception) {
                // 5. Error: Stop the loading spinner and set error message
                _isLoading.value = false
                _errorMessage.value = e.message ?: "An unknown error occurred"
            }
        }
    }
    fun refreshQuantitiesOnly() {
        // 1. Get the current lists from the LiveData
        val featured = _featuredProducts.value ?: return
        val brands = _popularBrands.value ?: return
        val recommended = _recommendedItems.value ?: return

        // 2. Re-run the Sync Loop for each list
        val allLists = listOf(featured, brands, recommended)
        allLists.forEach { list ->
            list.forEach { apiProduct ->
                val cartItem = CartManager.cartItems.find { it.id == apiProduct.id }
                apiProduct.quantity = cartItem?.quantity ?: 0
            }
        }

        // 3. Post the updated lists back to the UI
        _featuredProducts.value = featured
        _popularBrands.value = brands
        _recommendedItems.value = recommended
    }
}
