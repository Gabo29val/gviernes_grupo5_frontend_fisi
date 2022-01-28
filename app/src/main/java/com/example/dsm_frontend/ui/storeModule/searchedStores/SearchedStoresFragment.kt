package com.example.dsm_frontend.ui.storeModule.searchedStores

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.FragmentSearchedStoresBinding
import com.example.dsm_frontend.data.model.Store
import com.example.dsm_frontend.ui.storeModule.searchedStores.adapter.StoreAdapter

class SearchedStoresFragment : Fragment(R.layout.fragment_searched_stores) {

    private lateinit var mBinding: FragmentSearchedStoresBinding
    private lateinit var storeAdapter: StoreAdapter
    private lateinit var stores: ArrayList<Store>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentSearchedStoresBinding.bind(view)

        stores = ArrayList()
        storeAdapter = StoreAdapter(getStores())
        mBinding.rvStores.apply {
            adapter = storeAdapter
            setHasFixedSize(true)
        }
    }

    private fun getStores(): List<Store> {
        return listOf(
            Store(
                name = "Tienda prueba",
                rating = 5.0,
                photoUrl = "",
            ),
            Store(
                name = "Tienda prueba",
                rating = 5.0,
                photoUrl = "",
            ),
            Store(
                name = "Tienda prueba",
                rating = 5.0,
                photoUrl = "",
            ),
            Store(
                name = "Tienda prueba",
                rating = 5.0,
                photoUrl = "",
            ),
        )
    }
}