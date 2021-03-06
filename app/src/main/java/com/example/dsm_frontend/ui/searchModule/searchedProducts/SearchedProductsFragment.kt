package com.example.dsm_frontend.ui.searchModule.searchedProducts

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dsm_frontend.MainActivity
import com.example.dsm_frontend.R
import com.example.dsm_frontend.api.RetrofitClient
import com.example.dsm_frontend.core.Resource
import com.example.dsm_frontend.data.MinimarketDataSource
import com.example.dsm_frontend.databinding.FragmentSearchedProductsBinding
import com.example.dsm_frontend.data.model.Product
import com.example.dsm_frontend.presentation.ProductViewModel
import com.example.dsm_frontend.presentation.ProductViewModelFactory
import com.example.dsm_frontend.repository.MinimarketRepositoryImpl
import com.example.dsm_frontend.ui.searchModule.mainSearch.MainSearchFragmentDirections
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

    private var mActivity: MainActivity? = null

    private val arg by navArgs<SearchedProductsFragmentArgs>()

    private val mProductVM by viewModels<ProductViewModel> {
        ProductViewModelFactory(
            MinimarketRepositoryImpl(
                MinimarketDataSource(
                    RetrofitClient.apiService
                )
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentSearchedProductsBinding.bind(view)

        products = arrayListOf()
        mProductAdapter = ProductAdapter(products, this)

        mBinding.rvProducts.apply {
            adapter = mProductAdapter
            setHasFixedSize(true)
        }

        mActivity = activity as? MainActivity

        getProductByWord(arg.word)
        //setupActionBar()
        setupSearchView()
    }

    private fun setupActionBar() {

        mActivity?.setSupportActionBar(mBinding.toolbar)
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        /*//indicamos que muestre la flecha de retroceso
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true) //back
        mActivity?.supportActionBar?.setTitle(R.string.edit_store_title_add) //title
        mActivity?.supportActionBar?.title =
            if (mIsEditMode) getString(R.string.edit_store_title_edit)
            else getString(R.string.edit_store_title_add)
        setHasOptionsMenu(true) //que tenga acceso al menu*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mActivity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getProductByWord(word: String) {
        mProductVM.getProductByWord(word).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                    (mBinding.rvProducts.adapter as ProductAdapter).setData(listOf())
                }
                is Resource.Success -> {
                    mBinding.progressBar.visibility = View.GONE
                    if (result.data.isNotEmpty()) {
                        (mBinding.rvProducts.adapter as ProductAdapter).setData(result.data)
                    } else {
                        Toast.makeText(
                            mBinding.root.context,
                            "No hay productos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Failure -> {
                    mBinding.progressBar.visibility = View.GONE
                    Toast.makeText(mBinding.root.context, "Error de servidor", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun setupSearchView() {
        val item = mBinding.toolbar.menu.findItem(R.id.action_search)
        val searchView: SearchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    getProductByWord(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

/*    private fun getProductsFirebase() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("products")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
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
    }*/
/*

    private fun getProducts(): List<Product> {
        return listOf(
            Product(
                name = "Red label 1",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcoh??lica a base de malta fermentada de cereales como cebada, trigo, centeno y ma??z, que se destila y a??eja en barriles de madera tradicionalmente de roble blanco. Este ??ltimo proceso dura por lo menos tres a??os para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus or??genes en Irlanda y Escocia y en la actualidad se disfruta en muchos pa??ses a nivel mundial.",
                specifications = mutableListOf(
                    Pair("Presentaci??n", "Botella"),
                    Pair("Composici??n", "Grano y malta"),
                    Pair("Proceso de a??ejamiento", "No declarada"),
                    Pair("Volumen neto", "750ml"),
                )
            ),
            Product(
                name = "Red label 2",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcoh??lica a base de malta fermentada de cereales como cebada, trigo, centeno y ma??z, que se destila y a??eja en barriles de madera tradicionalmente de roble blanco. Este ??ltimo proceso dura por lo menos tres a??os para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus or??genes en Irlanda y Escocia y en la actualidad se disfruta en muchos pa??ses a nivel mundial.",
                specifications = mutableListOf(
                    Pair("Presentaci??n", "Botella"),
                    Pair("Composici??n", "Grano y malta"),
                    Pair("Proceso de a??ejamiento", "No declarada"),
                    Pair("Volumen neto", "750ml"),
                )
            ),
            Product(
                name = "Red label 3",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcoh??lica a base de malta fermentada de cereales como cebada, trigo, centeno y ma??z, que se destila y a??eja en barriles de madera tradicionalmente de roble blanco. Este ??ltimo proceso dura por lo menos tres a??os para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus or??genes en Irlanda y Escocia y en la actualidad se disfruta en muchos pa??ses a nivel mundial.",
                specifications = mutableListOf(
                    Pair("Presentaci??n", "Botella"),
                    Pair("Composici??n", "Grano y malta"),
                    Pair("Proceso de a??ejamiento", "No declarada"),
                    Pair("Volumen neto", "750ml"),
                )
            ),
            Product(
                name = "Red label 4",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcoh??lica a base de malta fermentada de cereales como cebada, trigo, centeno y ma??z, que se destila y a??eja en barriles de madera tradicionalmente de roble blanco. Este ??ltimo proceso dura por lo menos tres a??os para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus or??genes en Irlanda y Escocia y en la actualidad se disfruta en muchos pa??ses a nivel mundial.",
                specifications = mutableListOf(
                    Pair("Presentaci??n", "Botella"),
                    Pair("Composici??n", "Grano y malta"),
                    Pair("Proceso de a??ejamiento", "No declarada"),
                    Pair("Volumen neto", "750ml"),
                )
            ),
            Product(
                name = "Red label 5",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcoh??lica a base de malta fermentada de cereales como cebada, trigo, centeno y ma??z, que se destila y a??eja en barriles de madera tradicionalmente de roble blanco. Este ??ltimo proceso dura por lo menos tres a??os para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus or??genes en Irlanda y Escocia y en la actualidad se disfruta en muchos pa??ses a nivel mundial.",
                specifications = mutableListOf(
                    Pair("Presentaci??n", "Botella"),
                    Pair("Composici??n", "Grano y malta"),
                    Pair("Proceso de a??ejamiento", "No declarada"),
                    Pair("Volumen neto", "750ml"),
                )
            ),
            Product(
                name = "Red label 6",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcoh??lica a base de malta fermentada de cereales como cebada, trigo, centeno y ma??z, que se destila y a??eja en barriles de madera tradicionalmente de roble blanco. Este ??ltimo proceso dura por lo menos tres a??os para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus or??genes en Irlanda y Escocia y en la actualidad se disfruta en muchos pa??ses a nivel mundial.",
                specifications = mutableListOf(
                    Pair("Presentaci??n", "Botella"),
                    Pair("Composici??n", "Grano y malta"),
                    Pair("Proceso de a??ejamiento", "No declarada"),
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