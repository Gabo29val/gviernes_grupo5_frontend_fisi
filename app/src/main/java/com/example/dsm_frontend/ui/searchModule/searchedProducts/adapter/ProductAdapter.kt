package com.example.dsm_frontend.ui.searchModule.searchedProducts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.data.model.Car
import com.example.dsm_frontend.data.model.ItemCar
import com.example.dsm_frontend.databinding.ItemProductCardBinding
import com.example.dsm_frontend.data.model.Product

class ProductAdapter(var products: List<Product>, val itemClickListener: OnProductClickListener) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemProductCardBinding.bind(view)
        fun setData(product: Product) {
            binding.apply {
                tvNameProduct.text = product.name
                tvNameStore.text = product.nameStore
                tvPriceProduct.text = product.price.toString()
                Glide.with(this.root)
                    .load(product.photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imgPhotoProduct)

                root.setOnClickListener {
                    itemClickListener.onProductClick(product)
                }

                fabAddCar.setOnClickListener {
                    val item = ItemCar(product = product, amount = 1)
                    Car.addItem(item)
                    Toast.makeText(binding.root.context, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setData(products: List<Product>){
        this.products = products
        notifyDataSetChanged()
    }

    interface OnProductClickListener {
        fun onProductClick(product: Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_card, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products.get(position)
        holder.setData(product)
    }

    override fun getItemCount(): Int = products.size
}