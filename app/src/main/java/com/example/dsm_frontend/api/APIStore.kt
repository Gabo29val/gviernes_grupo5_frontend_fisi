package com.example.dsm_frontend.api

import com.example.dsm_frontend.model.Store
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface APIStore {
    @GET("all")
    fun getAllStores(): Call<List<Store>>

    @GET("stores/{lat},{lon},{radius}")
    fun getCloseStores(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Path("radius") radius: Double
    ): Call<List<Store>>
}