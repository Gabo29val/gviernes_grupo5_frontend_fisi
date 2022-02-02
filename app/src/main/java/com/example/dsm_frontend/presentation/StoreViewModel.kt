package com.example.dsm_frontend.presentation

import androidx.lifecycle.*
import com.example.dsm_frontend.core.Resource
import com.example.dsm_frontend.data.model.Store
import com.example.dsm_frontend.repository.MinimarketRepository
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class StoreViewModel(private val repo: MinimarketRepository) : ViewModel() {

    private val closeStoresMLD = MutableLiveData<List<Store>>()
    val closeStoresLD: LiveData<List<Store>> = closeStoresMLD

    private val radiusMLD = MutableLiveData<Double>().apply { value = 0.2 }
    val radiusLD: LiveData<Double> = radiusMLD

    fun setRadius(radius: Double) {
        radiusMLD.postValue(radius)
    }


    fun updateStores(stores: List<Store>) {
        closeStoresMLD.postValue(stores)
    }

    fun getAllStores() = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(repo.getAllStores())
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun getCloseStores(lat: Double, lon: Double) = liveData(Dispatchers.IO) {
        radiusLD.value?.let {
            val radiusMi = Math.round(((it) * 0.62137) * 100.0) / 100.0

            emit(Resource.Loading)
            try {
                emit(repo.getCloseStores(lat, lon, radiusMi))
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
    }

}

class StoreViewModelFactory(private val repo: MinimarketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MinimarketRepository::class.java).newInstance(repo)
    }
}