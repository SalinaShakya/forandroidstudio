package com.example.myview
import retrofit2.http.GET
interface ApiService {
    @GET("products")
    suspend fun getFeaturedProducts(): List<ProductResponse>
    @GET("products/category/electronics")
    suspend fun getHotDeals(): List<ProductResponse>
    @GET("https://dummyjson.com/products/categories")
    suspend fun getPopularCategories(): List<Popular>

}