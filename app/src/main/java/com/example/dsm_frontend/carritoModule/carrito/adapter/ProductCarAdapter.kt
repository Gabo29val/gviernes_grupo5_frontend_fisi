package com.example.dsm_frontend.carritoModule.carrito.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.ItemProductCardBinding
import com.example.dsm_frontend.databinding.ItemProductCarritoBinding
import com.example.dsm_frontend.model.Product

class ProductCarAdapter(val products: List<Product>): RecyclerView.Adapter<ProductCarAdapter.ProductViewHolder>(){
    inner class ProductViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemProductCarritoBinding.bind(view)
        fun setData(product: Product){
            binding.apply {
                tvNameProduct.text = product.name
                tvNameStore.text = product.nameStore
                tvPriceProduct.text = product.price.toString()

                Glide.with(this.root)
                    .load(product.photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imgPhotoProduct)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_carrito, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products.get(position)
        holder.setData(product)
    }

    override fun getItemCount(): Int = products.size
}