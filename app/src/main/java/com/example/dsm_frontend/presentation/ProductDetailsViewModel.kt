package com.example.dsm_frontend.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dsm_frontend.data.model.Store

class ProductDetailsViewModel : ViewModel() {
    private val cantidadMLD = MutableLiveData<Int>().apply { value = 1 }
    val cantidadLD: LiveData<Int> = cantidadMLD

    fun incrementa() {
        cantidadMLD.postValue(cantidadMLD.value?.inc())
    }

    fun decrementa() {
        cantidadMLD.postValue(cantidadMLD.value?.dec())
    }
}