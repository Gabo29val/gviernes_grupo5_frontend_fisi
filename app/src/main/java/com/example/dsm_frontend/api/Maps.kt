package com.example.dsm_frontend.api

import android.location.Location

object Maps {
    fun calculateDistanceKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val currentLocation = Location("Punto 1")
        currentLocation.latitude = lat1
        currentLocation.longitude = lon1
        val storeLocation = Location("Punto 2")
        storeLocation.latitude = lat2
        storeLocation.longitude = lon2
        return (Math.round(
            (currentLocation.distanceTo(storeLocation).toDouble() / 1000) * 100.0
        ) / 100.0)
    }
}