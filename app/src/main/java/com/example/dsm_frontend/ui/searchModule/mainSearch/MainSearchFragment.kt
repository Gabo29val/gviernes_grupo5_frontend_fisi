package com.example.dsm_frontend.ui.searchModule.mainSearch

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.FragmentMainSearchBinding

class MainSearchFragment : Fragment(R.layout.fragment_main_search) {

    private lateinit var mBinding: FragmentMainSearchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentMainSearchBinding.bind(view)

        /*mBinding.fabSearch.setOnClickListener {
            findNavController().navigate(R.id.action_mainSearchFragment_to_searchedProductsFragment)
        }*/

        setHasOptionsMenu(true)
        setupSearchView()
        setupCategories()
    }

    private fun setupSearchView() {
        val item = mBinding.toolbar.menu.findItem(R.id.action_search)
        val searchView: SearchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val action = MainSearchFragmentDirections.actionMainSearchFragmentToSearchedProductsFragment(it)
                    findNavController().navigate(action)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupCategories() {
        //categoria 1
        mBinding.category1.tvNameCategory.text = "Abarrotes"
        loadImg("https://patzcuaronoticias.com/wp-content/uploads/2020/04/%C2%BFCo%CC%81mo-solicitar-la-despensa-del-Plan-Alimentario-en-Michoaca%CC%81n.png", mBinding.category1.imgPhotoCategory)
        mBinding.category1.card.setOnClickListener {
            findNavController().navigate(R.id.action_mainSearchFragment_to_searchedProductsFragment)
        }

        //categoria 2
        mBinding.category2.tvNameCategory.text = "Lácteos y huevos"
        loadImg("https://us.123rf.com/450wm/nikitos77/nikitos771204/nikitos77120400037/13431200-productos-l%C3%A1cteos-el-queso-y-los-huevos-sobre-un-fondo-blanco.jpg?ver=6", mBinding.category2.imgPhotoCategory)
        mBinding.category2.card.setOnClickListener {
            findNavController().navigate(R.id.action_mainSearchFragment_to_searchedProductsFragment)
        }

        //categoria 3
        mBinding.category3.tvNameCategory.text = "Bebidas"
        loadImg("https://i.pinimg.com/originals/6d/b3/f2/6db3f2227c414ac2dba117d5a6f3af15.png", mBinding.category3.imgPhotoCategory)
        mBinding.category3.card.setOnClickListener {
            findNavController().navigate(R.id.action_mainSearchFragment_to_searchedProductsFragment)
        }
    }

    private fun loadImg(url: String, view: ImageView){
        Glide.with(mBinding.root)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(view)
    }

   /* private fun setupTabs() {
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
    }*/
}