package com.example.dsm_appcliente.searchModule.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.dsm_appcliente.searchModule.search.adapter.ViewPagerAdapter
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.FragmentMainSearchBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainSearchFragment : Fragment(R.layout.fragment_main_search) {

    private lateinit var mBinding: FragmentMainSearchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentMainSearchBinding.bind(view)

        /*mBinding.fabSearch.setOnClickListener {
            findNavController().navigate(R.id.action_mainSearchFragment_to_searchedProductsFragment)
        }*/

        setHasOptionsMenu(true)
        setupTabs()
        setupSearchView()
    }

    private fun setupSearchView() {
        val item = mBinding.toolbar.menu.findItem(R.id.action_search)
        val searchView: SearchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                findNavController().navigate(R.id.action_mainSearchFragment_to_searchedProductsFragment)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupTabs() {
        val adapter = activity?.let { ViewPagerAdapter(it.supportFragmentManager, it.lifecycle) }
        mBinding.viewPager.adapter = adapter
        //mBinding.viewPager.reduceDragSensitivity()

        mBinding.viewPager.requestDisallowInterceptTouchEvent(true)

        TabLayoutMediator(mBinding.tabs, mBinding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Categorias"
                1 -> tab.text = "Tiendas Cercanas"
            }
        }.attach()
    }

    fun ViewPager2.reduceDragSensitivity() {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView

        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop * 6) // "6" was obtained experimentally
    }
}