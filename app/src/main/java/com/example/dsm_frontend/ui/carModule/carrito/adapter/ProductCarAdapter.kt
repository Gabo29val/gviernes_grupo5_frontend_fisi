package com.example.dsm_frontend.ui.carModule.carrito.adapter

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
import com.example.dsm_frontend.databinding.ItemProductCarritoBinding
import com.example.dsm_frontend.data.model.Product
import com.example.dsm_frontend.presentation.MainCarViewModel
import com.example.dsm_frontend.presentation.ProductDetailsViewModel

class ProductCarAdapter(private val viewModel: MainCarViewModel) :
    RecyclerView.Adapter<ProductCarAdapter.ProductViewHolder>() {

    private val PAYLOAD_AMOUNT = "payload_amount"

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemProductCarritoBinding.bind(view)
        fun setData(item: ItemCar, position: Int) {
            binding.apply {
                tvNameProduct.text = item.product?.name
                tvNameStore.text = item.product?.nameStore
                tvPriceProduct.text = "S/ ${item.product?.price.toString()}"
                tvCantidad.text = item.amount.toString()

                Glide.with(this.root)
                    .load(item.product?.photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imgPhotoProduct)

                btnAdd.setOnClickListener {
                    changeAmount((item.amount?.inc())!!, position)
                    viewModel.updateTotalAmount()
                }

                btnRemove.setOnClickListener {
                    changeAmount((item.amount?.dec())!!, position)
                    viewModel.updateTotalAmount()
                }
            }
        }
    }

    fun changeAmount(amount: Int, position: Int) {
        if (amount >= 0) {
            Car.items.get(position).amount = amount
            notifyItemChanged(position, PAYLOAD_AMOUNT)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_carrito, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = Car.items.get(position)
        holder.setData(product, position)
    }

    override fun getItemCount(): Int = Car.items.size

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        // if there are no payload objects, call the usual onBindViewHolder method
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            // compare each Object in the payloads to the PAYLOAD you provided to notifyItemChanged
            for (payload in payloads) {
                if (payload == PAYLOAD_AMOUNT) {
                    // we updated the title so let's only update that view!
                    //(holder as ProductViewHolder).txtTitle.setText(items.get(position).getTitle())
                    (holder as ProductViewHolder).binding.tvCantidad.text =
                        Car.items.get(position).amount.toString()
                } // else: handle any other possible PAYLOAD objects
            }
        }
    }
}