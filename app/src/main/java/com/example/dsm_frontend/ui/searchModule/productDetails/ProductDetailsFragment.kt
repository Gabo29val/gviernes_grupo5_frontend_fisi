package com.example.dsm_frontend.ui.searchModule.productDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.data.model.Car
import com.example.dsm_frontend.data.model.ItemCar
import com.example.dsm_frontend.databinding.DialogAddCarBinding
import com.example.dsm_frontend.databinding.FragmentProductDetailsBinding
import com.example.dsm_frontend.presentation.ProductDetailsViewModel
import com.example.dsm_frontend.ui.searchModule.productDetails.adapter.SpecificationProductAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private lateinit var mBinding: FragmentProductDetailsBinding
    private val arg by navArgs<ProductDetailsFragmentArgs>()

    private val mProductDetailsVM: ProductDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentProductDetailsBinding.bind(view)

        setupData()
        setupComponents()
    }

    private fun setupComponents() {

        mProductDetailsVM.cantidadLD.observe(viewLifecycleOwner, {
            mBinding.details.tvCantidad.text = it.toString()
        })

        mBinding.details.apply {
            btnAdd.setOnClickListener {
                mProductDetailsVM.incrementa()
            }
            btnRemove.setOnClickListener {
                mProductDetailsVM.decrementa()
            }
        }

        mBinding.fabAddCar.setOnClickListener {
            val cant = mProductDetailsVM.cantidadLD.value
            val item = ItemCar(product = arg.product, amount = cant)
            Car.addItem(item)

            val dialogView = layoutInflater.inflate(R.layout.dialog_add_car, null)
            val binding = DialogAddCarBinding.bind(dialogView)

            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(mBinding.root.context)
            materialAlertDialogBuilder.setView(dialogView)
            materialAlertDialogBuilder.setCancelable(false)
            val dialogBuilder = materialAlertDialogBuilder.show()

            binding.btnOk.setOnClickListener {
                dialogBuilder.dismiss()
            }
        }
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
        val adaptador = SpecificationProductAdapter(arg.product.specifications!!)
        mBinding.details.rvSpecifications.apply {
            adapter = adaptador
            setHasFixedSize(false)
        }
    }
}