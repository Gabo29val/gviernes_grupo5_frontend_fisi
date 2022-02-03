package com.example.dsm_frontend.data.model

data class ItemCar(
    var product: Product? = null,
    var amount: Int? = null
)

object Car {
    val items: ArrayList<ItemCar> by lazy {
        ArrayList()
    }

    fun addItem(item: ItemCar) {
        items.add(item)
    }

    fun clearItems(){
        items.clear()
    }
}