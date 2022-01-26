package com.example.dsm_frontend.searchModule.productDetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.ItemSpecificationProductBinding
import com.example.dsm_frontend.model.Specification

class SpecificationProductAdapter(val specifications: List<Specification>) :
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
        fun setData(specification: Specification) {
            binding.apply {
                binding.tvNameSpecification.text = specification.name
                binding.tvValorSpecification.text = specification.value
            }
        }
    }
}