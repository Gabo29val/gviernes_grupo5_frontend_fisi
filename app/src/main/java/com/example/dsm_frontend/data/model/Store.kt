package com.example.dsm_frontend.data.model

data class Store(
    var id: String? = null,
    var name: String? = null,
    var photoUrl: String? = null,
    var rating: Double? = null,
    var address: String? = null,
    var location: Location? = null
)

data class Location(
    var latitude: Double? = null,
    var longitude: Double? = null
)
