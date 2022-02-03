package com.example.dsm_frontend.data

import com.example.dsm_frontend.api.APIService
import com.example.dsm_frontend.core.Resource
import com.example.dsm_frontend.data.model.Product
import com.example.dsm_frontend.data.model.Store

class MinimarketDataSource(private val service: APIService) {
    suspend fun getAllStores(): Resource<List<Store>> {
        return Resource.Success(service.getAllStores())
    }

    suspend fun getCloseStores(
        lat: Double,
        lon: Double,
        radiusMi: Double
    ): Resource<List<Store>> {
        return Resource.Success(service.getCloseStores(lat, lon, radiusMi))
    }

    suspend fun getProductsByWord(word: String): Resource<List<Product>>{
        return Resource.Success(service.getProductsByWord(word))
    }
}