package com.example.dsm_frontend.repository

import com.example.dsm_frontend.core.Resource
import com.example.dsm_frontend.data.StoreDataSource
import com.example.dsm_frontend.data.model.Store

class StoreRepositoryImpl(private val datasource: StoreDataSource): MinimarketRepository {
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
}