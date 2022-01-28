package com.example.dsm_frontend.api

import com.example.dsm_frontend.data.model.Store
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface APIService {
    @GET("all")
    suspend fun getAllStores(): List<Store>

    /*@GET("stores/{lat},{lon},{radius}")
    fun getCloseStores(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Path("radius") radius: Double
    ): Call<List<Store>>*/

    @GET("stores/{lat},{lon},{radius}")
    suspend fun getCloseStores(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Path("radius") radius: Double
    ): List<Store>
}