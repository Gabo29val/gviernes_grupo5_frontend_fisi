package com.example.dsm_frontend.data

import com.example.dsm_frontend.api.APIService
import com.example.dsm_frontend.core.Resource
import com.example.dsm_frontend.data.model.Store

class StoreDataSource(private val webservice: APIService) {
    suspend fun getAllStores(): Resource<List<Store>> {
        return Resource.Success(webservice.getAllStores())
    }

    suspend fun getCloseStores(
        lat: Double,
        lon: Double,
        radiusMi: Double
    ): Resource<List<Store>> {
        return Resource.Success(webservice.getCloseStores(lat, lon, radiusMi))
    }
}