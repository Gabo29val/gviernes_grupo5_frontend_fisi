package com.example.dsm_frontend.repository

import com.example.dsm_frontend.core.Resource
import com.example.dsm_frontend.data.model.Product
import com.example.dsm_frontend.data.model.Store

interface MinimarketRepository {
    suspend fun getAllStores(): Resource<List<Store>>
    suspend fun getCloseStores(lat: Double, lon: Double, radiusMi: Double): Resource<List<Store>>

    suspend fun getProductsByWord(word: String): Resource<List<Product>>
}