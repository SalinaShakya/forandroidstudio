package com.example.myview.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // 2. Added an error LiveData (Professional practice)
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchHomeData() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Fetch all data in the background
                val products = RetrofitClient.apiService.getFeaturedProducts()
                _featuredProducts.value = products

                _hotDeals.value = RetrofitClient.apiService.getHotDeals()

                _popularCategories.value = RetrofitClient.apiService.getPopularCategories()

                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.message ?: "An unknown error occurred"
            }
        }
    }
}