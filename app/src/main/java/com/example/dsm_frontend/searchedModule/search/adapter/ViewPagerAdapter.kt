package com.example.dsm_appcliente.searchModule.search.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dsm_appcliente.searchModule.search.CategoriesFragment
import com.example.dsm_appcliente.searchModule.search.StoresFragment

class ViewPagerAdapter(supportFragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(supportFragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CategoriesFragment()
            1 -> StoresFragment()
            else -> Fragment()
        }
    }


}