package com.example.dsm_frontend.searchModule.productDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.FragmentProductDetailsBinding
import com.example.dsm_frontend.searchModule.productDetails.adapter.SpecificationProductAdapter

class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private lateinit var mBinding: FragmentProductDetailsBinding
    private val arg by navArgs<ProductDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentProductDetailsBinding.bind(view)

        setupData()
    }

    private fun setupData() {
        mBinding.apply {
            tvNameProduct.text = arg.product.name
            details.tvDescription.text = arg.product.description
            details.tvNameStore.text = arg.product.nameStore
            Glide.with(root)
                .load(arg.product.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imgPhotoProduct)
        }

        setupAdapterSpecification()
    }

    private fun setupAdapterSpecification() {
        val adaptador = SpecificationProductAdapter(arg.product.specifications)
        mBinding.details.rvSpecifications.apply {
            adapter = adaptador
            setHasFixedSize(true)
        }
    }
}