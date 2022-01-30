package com.example.dsm_frontend.presentation

import android.util.Log
import androidx.lifecycle.*
import com.example.dsm_frontend.api.APIService
import com.example.dsm_frontend.core.Resource
import com.example.dsm_frontend.data.model.Store
import com.example.dsm_frontend.repository.StoreRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class StoreViewModel(private val repo: StoreRepository) : ViewModel() {

    fun getAllStores() = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(repo.getAllStores())
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun getCloseStores(lat: Double, lon: Double, radiusMi: Double) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(repo.getCloseStores(lat, lon, radiusMi))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

}

class StoreViewModelFactory(private val repo: StoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(StoreRepository::class.java).newInstance(repo)
    }
}