package com.example.dsm_frontend.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dsm_frontend.data.model.Car
import com.example.dsm_frontend.data.model.ItemCar

class MainCarViewModel : ViewModel() {
    private val amountTotalMLD = MutableLiveData<Double>().apply {
        value = calculateTotalAmount()
    }

    val amountTotalLD: LiveData<Double> = amountTotalMLD

    fun updateTotalAmount() {
        amountTotalMLD.postValue(calculateTotalAmount())
    }

    private fun calculateTotalAmount(): Double {
        var amount: Double = 0.0
        for (item in Car.items) {
            val price = item.product?.price!!
            val cant = item.amount
            amount += cant!! * price
        }
        return Math.round((amount) * 100.0) / 100.0
    }
}