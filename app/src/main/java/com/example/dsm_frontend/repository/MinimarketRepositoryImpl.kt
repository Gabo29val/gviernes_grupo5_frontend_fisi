package com.example.dsm_frontend.repository

import com.example.dsm_frontend.core.Resource
import com.example.dsm_frontend.data.MinimarketDataSource
import com.example.dsm_frontend.data.model.Product
import com.example.dsm_frontend.data.model.Store

class MinimarketRepositoryImpl(private val datasource: MinimarketDataSource): MinimarketRepository {
    override suspend fun getAllStores(): Resource<List<Store>> {
        return datasource.getAllStores()
    }

    override suspend fun getCloseStores(
        lat: Double,
        lon: Double,
        radiusMi: Double
    ): Resource<List<Store>> {
        return datasource.getCloseStores(lat, lon, radiusMi)
    }

    override suspend fun getProductsByWord(word: String): Resource<List<Product>> {
        return datasource.getProductsByWord(word)
    }
}