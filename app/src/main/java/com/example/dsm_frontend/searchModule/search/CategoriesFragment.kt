package com.example.dsm_frontend.searchModule.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    private lateinit var mBinding: FragmentCategoriesBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentCategoriesBinding.bind(view)

        setupCategories()
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
}