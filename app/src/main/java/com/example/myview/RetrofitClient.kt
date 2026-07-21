package com.example.myview

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
    object RetrofitClient {


        // This is the clean base web address for the real test server
        private const val BASE_URL = "https://fakestoreapi.com/"

        val apiService: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
