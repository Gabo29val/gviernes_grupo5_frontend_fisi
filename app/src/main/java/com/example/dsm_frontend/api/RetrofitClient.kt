package com.example.dsm_frontend.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val apiService: APIService by lazy {
        Retrofit.Builder()
            .baseUrl("http://$addressIP:8080/minimarket/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(APIService::class.java)
    }

    val addressIP: String = "192.168.0.103"
}