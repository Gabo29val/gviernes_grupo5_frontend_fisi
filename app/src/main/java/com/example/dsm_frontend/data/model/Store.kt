package com.example.dsm_frontend.data.model

import java.io.Serializable

data class Store(
    var id: String? = null,
    var name: String? = null,
    var photoUrl: String? = null,
    var rating: Double? = null,
    var address: String? = null,
    var telephone: String? = null,
    var location: Location? = null
) : Serializable

data class Location(
    var latitude: Double? = null,
    var longitude: Double? = null
) : Serializable
