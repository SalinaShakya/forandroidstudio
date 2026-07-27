package com.example.myview.data.api

import com.example.myview.data.model.Popular
import com.example.myview.data.model.ProductResponse
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getFeaturedProducts(): List<ProductResponse>
    @GET("products/category/electronics")
    suspend fun getHotDeals(): List<ProductResponse>
    @GET("https://dummyjson.com/products/categories")
    suspend fun getPopularCategories(): List<Popular>

}