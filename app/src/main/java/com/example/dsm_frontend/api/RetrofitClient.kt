package com.example.dsm_frontend.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val webservice: APIService by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.0.106:8080/stores/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(APIService::class.java)
    }
}