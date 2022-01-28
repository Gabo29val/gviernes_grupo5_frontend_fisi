package com.example.dsm_frontend.carritoModule.carrito

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.dsm_frontend.R
import com.example.dsm_frontend.carritoModule.carrito.adapter.ProductCarAdapter
import com.example.dsm_frontend.databinding.FragmentMainCarBinding
import com.example.dsm_frontend.model.Product
import com.example.dsm_frontend.model.Specification

class MainCarFragment : Fragment(R.layout.fragment_main_car) {

    private lateinit var mBinding: FragmentMainCarBinding
    private lateinit var mProductAdapter: ProductCarAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentMainCarBinding.bind(view)

        mProductAdapter = ProductCarAdapter(getProducts())

        mBinding.rvProducts.apply {
            adapter = mProductAdapter
            setHasFixedSize(true)
        }
    }

    private fun getProducts(): List<Product> {
        return listOf(
            Product(
                name = "Red label 1",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcohólica a base de malta fermentada de cereales como cebada, trigo, centeno y maíz, que se destila y añeja en barriles de madera tradicionalmente de roble blanco. Este último proceso dura por lo menos tres años para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus orígenes en Irlanda y Escocia y en la actualidad se disfruta en muchos países a nivel mundial.",
                stock = 10,
                specifications = mutableListOf(
                    Specification("Presentación", "Botella"),
                    Specification("Composición", "Grano y malta"),
                    Specification("Proceso de añejamiento", "No declarada"),
                    Specification("Volumen neto", "750ml"),
                )
            ),
            Product(
                name = "Red label 2",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcohólica a base de malta fermentada de cereales como cebada, trigo, centeno y maíz, que se destila y añeja en barriles de madera tradicionalmente de roble blanco. Este último proceso dura por lo menos tres años para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus orígenes en Irlanda y Escocia y en la actualidad se disfruta en muchos países a nivel mundial.",
                stock = 10,
                specifications = mutableListOf(
                    Specification("Presentación", "Botella"),
                    Specification("Composición", "Grano y malta"),
                    Specification("Proceso de añejamiento", "No declarada"),
                    Specification("Volumen neto", "750ml"),
                )
            ),
        )
    }
}