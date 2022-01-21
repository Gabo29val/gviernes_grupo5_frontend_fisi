package com.example.dsm_appcliente.searchModule.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.FragmentStoresBinding

class StoresFragment : Fragment(R.layout.fragment_stores)/*, OnMapReadyCallback*/ {

    private lateinit var mBinding: FragmentStoresBinding
    /*private lateinit var mMap: GoogleMap*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentStoresBinding.bind(view)

        setupComponents()

        /*val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)*/


    }

    private fun setupComponents() {
    }

    /*override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }*/
}