package com.example.dsm_frontend.api

import com.example.dsm_frontend.data.model.Product
import com.example.dsm_frontend.data.model.Store
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface APIService {
    @GET("stores/all")
    suspend fun getAllStores(): List<Store>

    @GET("stores/close/{lat},{lon},{radius}")
    suspend fun getCloseStores(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Path("radius") radius: Double
    ): List<Store>

    @GET("products/search/{word}")
    suspend fun getProductsByWord(
        @Path("word") word: String
    ): List<Product>


}