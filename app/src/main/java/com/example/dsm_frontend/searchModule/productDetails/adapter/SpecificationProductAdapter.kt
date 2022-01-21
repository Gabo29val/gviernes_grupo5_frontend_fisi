package com.example.dsm_frontend.searchModule.productDetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.ItemSpecificationProductBinding

class SpecificationProductAdapter(val specifications: MutableList<Pair<String, String>>) :
    RecyclerView.Adapter<SpecificationProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_specification_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pair = specifications.get(position)
        holder.setData(pair)
    }

    override fun getItemCount(): Int = specifications.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemSpecificationProductBinding.bind(view)
        fun setData(pair: Pair<String, String>) {
            binding.apply {
                binding.tvNameSpecification.text = pair.first
                binding.tvValorSpecification.text = pair.second
            }
        }
    }
}