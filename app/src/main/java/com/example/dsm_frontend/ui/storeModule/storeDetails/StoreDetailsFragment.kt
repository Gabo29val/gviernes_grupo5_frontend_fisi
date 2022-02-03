package com.example.dsm_frontend.ui.storeModule.storeDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.FragmentStoreDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class StoreDetailsFragment : Fragment(R.layout.fragment_store_details), OnMapReadyCallback {

    private lateinit var mBinding: FragmentStoreDetailsBinding

    private val arg by navArgs<StoreDetailsFragmentArgs>()

    private lateinit var mMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentStoreDetailsBinding.bind(view)
        loadMap()
        setupData()
    }

    private fun loadMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_store) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupData() {
        mBinding.apply {
            tvNameStore.text = arg.store.name
            details.tvTelephone.text = arg.store.telephone
            details.tvAddress.text = arg.store.address
            details.rating.rating = (arg.store.rating?.toFloat() ?: 0.0) as Float
            Glide.with(this.root)
                .load(arg.store.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mBinding.imgPhotoStore)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val markOption = MarkerOptions()
            .position(LatLng(arg.store.location?.latitude!!, arg.store.location?.longitude!!))

        mMap.addMarker(markOption)

        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(arg.store.location?.latitude!!, arg.store.location?.longitude!!),
                17f
            )
        );
    }

}