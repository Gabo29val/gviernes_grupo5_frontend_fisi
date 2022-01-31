package com.example.dsm_frontend.ui.searchModule.searchedProducts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.FragmentSearchedProductsBinding
import com.example.dsm_frontend.data.model.Product
import com.example.dsm_frontend.ui.searchModule.searchedProducts.adapter.ProductAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchedProductsFragment : Fragment(R.layout.fragment_searched_products),
    ProductAdapter.OnProductClickListener {
    private lateinit var mBinding: FragmentSearchedProductsBinding
    private lateinit var mProductAdapter: ProductAdapter
    private lateinit var products: ArrayList<Product>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentSearchedProductsBinding.bind(view)

        products = arrayListOf()
        mProductAdapter = ProductAdapter(products, this)

        mBinding.rvProducts.apply {
            adapter = mProductAdapter
            setHasFixedSize(true)
        }

        getProductsFirebase()
    }

    private fun getProductsFirebase() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("products")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (productSnapshot in snapshot.children){
                        val product = productSnapshot.getValue(Product::class.java)
                        product?.id = productSnapshot.key
                        products.add(product!!)
                    }
                }
                mBinding.rvProducts.adapter?.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
/*

    private fun getProducts(): List<Product> {
        return listOf(
            Product(
                name = "Red label 1",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcohólica a base de malta fermentada de cereales como cebada, trigo, centeno y maíz, que se destila y añeja en barriles de madera tradicionalmente de roble blanco. Este último proceso dura por lo menos tres años para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus orígenes en Irlanda y Escocia y en la actualidad se disfruta en muchos países a nivel mundial.",
                specifications = mutableListOf(
                    Pair("Presentación", "Botella"),
                    Pair("Composición", "Grano y malta"),
                    Pair("Proceso de añejamiento", "No declarada"),
                    Pair("Volumen neto", "750ml"),
                )
            ),
            Product(
                name = "Red label 2",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcohólica a base de malta fermentada de cereales como cebada, trigo, centeno y maíz, que se destila y añeja en barriles de madera tradicionalmente de roble blanco. Este último proceso dura por lo menos tres años para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus orígenes en Irlanda y Escocia y en la actualidad se disfruta en muchos países a nivel mundial.",
                specifications = mutableListOf(
                    Pair("Presentación", "Botella"),
                    Pair("Composición", "Grano y malta"),
                    Pair("Proceso de añejamiento", "No declarada"),
                    Pair("Volumen neto", "750ml"),
                )
            ),
            Product(
                name = "Red label 3",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcohólica a base de malta fermentada de cereales como cebada, trigo, centeno y maíz, que se destila y añeja en barriles de madera tradicionalmente de roble blanco. Este último proceso dura por lo menos tres años para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus orígenes en Irlanda y Escocia y en la actualidad se disfruta en muchos países a nivel mundial.",
                specifications = mutableListOf(
                    Pair("Presentación", "Botella"),
                    Pair("Composición", "Grano y malta"),
                    Pair("Proceso de añejamiento", "No declarada"),
                    Pair("Volumen neto", "750ml"),
                )
            ),
            Product(
                name = "Red label 4",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcohólica a base de malta fermentada de cereales como cebada, trigo, centeno y maíz, que se destila y añeja en barriles de madera tradicionalmente de roble blanco. Este último proceso dura por lo menos tres años para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus orígenes en Irlanda y Escocia y en la actualidad se disfruta en muchos países a nivel mundial.",
                specifications = mutableListOf(
                    Pair("Presentación", "Botella"),
                    Pair("Composición", "Grano y malta"),
                    Pair("Proceso de añejamiento", "No declarada"),
                    Pair("Volumen neto", "750ml"),
                )
            ),
            Product(
                name = "Red label 5",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcohólica a base de malta fermentada de cereales como cebada, trigo, centeno y maíz, que se destila y añeja en barriles de madera tradicionalmente de roble blanco. Este último proceso dura por lo menos tres años para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus orígenes en Irlanda y Escocia y en la actualidad se disfruta en muchos países a nivel mundial.",
                specifications = mutableListOf(
                    Pair("Presentación", "Botella"),
                    Pair("Composición", "Grano y malta"),
                    Pair("Proceso de añejamiento", "No declarada"),
                    Pair("Volumen neto", "750ml"),
                )
            ),
            Product(
                name = "Red label 6",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcohólica a base de malta fermentada de cereales como cebada, trigo, centeno y maíz, que se destila y añeja en barriles de madera tradicionalmente de roble blanco. Este último proceso dura por lo menos tres años para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus orígenes en Irlanda y Escocia y en la actualidad se disfruta en muchos países a nivel mundial.",
                specifications = mutableListOf(
                    Pair("Presentación", "Botella"),
                    Pair("Composición", "Grano y malta"),
                    Pair("Proceso de añejamiento", "No declarada"),
                    Pair("Volumen neto", "750ml"),
                )
            ),
        )
    }
*/

    override fun onProductClick(product: Product) {

        val action = SearchedProductsFragmentDirections
            .actionSearchedProductsFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }
}