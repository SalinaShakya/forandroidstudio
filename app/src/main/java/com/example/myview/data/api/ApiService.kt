package com.example.myview.data.api

import com.example.myview.data.model.Popular
import com.example.myview.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    suspend fun getFeaturedProducts(): List<ProductResponse>
    @GET("products/category/electronics")
    suspend fun getHotDeals(): List<ProductResponse>
    @GET("https://dummyjson.com/products/categories")
    suspend fun getPopularCategories(): List<Popular>
    @GET("products/{id}") 
    suspend fun getProductDetails(@Path("id") productId: Int): ProductResponse

}