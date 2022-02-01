package com.example.dsm_frontend.ui.storeModule.storeDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.FragmentStoreDetailsBinding

class StoreDetailsFragment : Fragment(R.layout.fragment_store_details) {

    private lateinit var mBinding: FragmentStoreDetailsBinding

    private val arg by navArgs<StoreDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentStoreDetailsBinding.bind(view)

        Glide.with(this)
            .load(arg.store.photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(mBinding.imgPhotoStore)
    }

}