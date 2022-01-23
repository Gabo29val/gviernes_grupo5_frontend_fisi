package com.example.dsm_frontend.model

import java.io.Serializable

data class Product(
    val id: Long = 0,
    val name: String = "",
    val nameStore: String = "",
    val price: Double = 0.0,
    val photoUrl: String = "",
    val description: String = "",

    val specifications: MutableList<Pair<String, String>>
): Serializable