package com.example.dsm_frontend.model

import java.io.Serializable

data class Product(
    var id: String? = null,
    var name: String? = null,
    var nameStore: String? = null,
    var price: Double? = null,
    var photoUrl: String? = null,
    var description: String? = null,
    var stock: Int? = null,
    var specifications: List<Specification>? = null
) : Serializable

data class Specification(
    val name: String? = null,
    val value: String? = null
) : Serializable