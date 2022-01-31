package com.example.dsm_frontend.ui.storeModule.searchedStores

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.dsm_frontend.R
import com.example.dsm_frontend.api.RetrofitClient
import com.example.dsm_frontend.core.Resource
import com.example.dsm_frontend.data.StoreDataSource
import com.example.dsm_frontend.databinding.FragmentSearchedStoresBinding
import com.example.dsm_frontend.presentation.StoreViewModel
import com.example.dsm_frontend.presentation.StoreViewModelFactory
import com.example.dsm_frontend.repository.StoreRepositoryImpl
import com.example.dsm_frontend.ui.storeModule.searchedStores.adapter.StoreAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class SearchedStoresFragment : Fragment(R.layout.fragment_searched_stores) {

    private lateinit var mBinding: FragmentSearchedStoresBinding

    private val mStoreViewModel: StoreViewModel by viewModels {
        StoreViewModelFactory(
            StoreRepositoryImpl(
                StoreDataSource(
                    RetrofitClient.apiService
                )
            )
        )
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentPosition: Location

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentSearchedStoresBinding.bind(view)
        mBinding.rvStores.apply {
            adapter = StoreAdapter(listOf())
            setHasFixedSize(true)
        }

        getCurrentLocation()
        getStores()
    }

    private fun getStores() {
        mStoreViewModel.getAllStores().observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    mBinding.progressBar.visibility = View.GONE
                    (mBinding.rvStores.adapter as StoreAdapter).setStores(result.data, currentPosition)
                }
                is Resource.Failure -> {
                    Toast.makeText(mBinding.root.context, "Error de servidor", Toast.LENGTH_SHORT)
                        .show()
                    mBinding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    //metodo que pide ultima ubicacion registrada del dispositivo
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mBinding.root.context)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            try {
                currentPosition = Location("Ubicacion")
                currentPosition.latitude = it.latitude
                currentPosition.longitude = it.longitude

            } catch (e: Exception) {
                Log.e("error", e.message!!)
            }
        }
    }
}