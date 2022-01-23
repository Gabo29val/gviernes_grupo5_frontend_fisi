package com.example.dsm_frontend.storeModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.FragmentStoresBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainStoreFragment : Fragment(R.layout.fragment_main_store), OnMapReadyCallback {

    private lateinit var mBinding: FragmentStoresBinding
    private lateinit var mMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentStoresBinding.bind(view)

        setupComponents()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupComponents() {
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val ubi = LatLng(-12.0572215,-77.0854449)
        mMap.addMarker(MarkerOptions().position(ubi).title("UNMSM"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ubi))
    }
}