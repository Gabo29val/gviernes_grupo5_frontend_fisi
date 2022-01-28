package com.example.dsm_frontend.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dsm_frontend.api.APIStore
import com.example.dsm_frontend.model.Store
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StoreViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.106:8080/stores/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: APIStore = retrofit.create(APIStore::class.java)

    private var stores = MutableLiveData<List<Store>>()

    fun getStores(): LiveData<List<Store>> {
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
    }

    fun getCloseStores(lat: Double, lon: Double, radiusMi: Double): LiveData<List<Store>> {
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
    }
}