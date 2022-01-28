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
/*
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.106:8080/stores/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: APIService = retrofit.create(APIService::class.java)
*/

    //private var stores = MutableLiveData<List<Store>>()

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


/*    fun getStores(): LiveData<List<Store>> {
        val call = service.getAllStores()
        call.enqueue(object : Callback<List<Store>> {
            override fun onResponse(call: Call<List<Store>>, response: Response<List<Store>>) {
                val resp = response.body()
                resp.let {
                    stores.postValue(it)
                    Log.d("TIENDAS", it.toString())
                }
            }

            override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                t.message?.let { Log.d("ERRRORRRRRRRRR", it) }
            }
        })

        return stores
    }*/

/*    fun getCloseStores(lat: Double, lon: Double, radiusMi: Double): LiveData<List<Store>> {
        val call = service.getCloseStores(lat, lon, radiusMi)
        Log.d("CONSULTA", "lat: $lat, lon: $lon, radius: $radiusMi")
        call.enqueue(object : Callback<List<Store>> {
            override fun onResponse(call: Call<List<Store>>, response: Response<List<Store>>) {
                val resp = response.body()
                resp.let {
                    stores.postValue(it)
                    Log.d("TIENDAS", it.toString())
                }
            }

            override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                t.message?.let { Log.d("ERRRORRRRRRRRR", it) }
            }
        })
        return stores
    }*/
}

class StoreViewModelFactory(private val repo: StoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(StoreRepository::class.java).newInstance(repo)
    }
}