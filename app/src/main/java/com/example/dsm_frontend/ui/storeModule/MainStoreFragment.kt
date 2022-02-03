package com.example.dsm_frontend.ui.storeModule

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.api.Maps
import com.example.dsm_frontend.api.RetrofitClient
import com.example.dsm_frontend.core.Resource
import com.example.dsm_frontend.data.MinimarketDataSource
import com.example.dsm_frontend.databinding.FragmentMainStoreBinding
import com.example.dsm_frontend.data.model.Store
import com.example.dsm_frontend.presentation.StoreViewModel
import com.example.dsm_frontend.presentation.StoreViewModelFactory
import com.example.dsm_frontend.repository.MinimarketRepositoryImpl
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory


class MainStoreFragment : Fragment(R.layout.fragment_main_store), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMarkerClickListener {

    private val mStoreViewModel by navGraphViewModels<StoreViewModel>(R.id.main_graph) {
        StoreViewModelFactory(
            MinimarketRepositoryImpl(
                MinimarketDataSource(
                    RetrofitClient.apiService
                )
            )
        )
    }

    private lateinit var mBinding: FragmentMainStoreBinding
    private lateinit var mMap: GoogleMap
    private var currentPosition: Location? = null
    private var circle: Circle? = null
    private val allMarkers = ArrayList<Marker>()
    //private var radius: Double = 0.10 //EN KM

    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private var selectedStore: Store? = null

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentMainStoreBinding.bind(view)

        loadMap()
        setupComponents()
        setupSearchView()
    }


    /**
     * Metodo para cargar el mapa en el fragmento
     * */
    fun loadMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Metodo para inicializar valores de componentes
     * */
    private fun setupComponents() {
        mBinding.frameInfoTienda.visibility = View.GONE

        mBinding.btnAplicar.setOnClickListener {
            //aqui hace la consulta con el nuevo radio
            getCloseStore()
        }

        mBinding.cardInfoStore.btnGoToStore.setOnClickListener {
            selectedStore?.let {
                val action =
                    MainStoreFragmentDirections.actionMainStoreFragmentToStoreDetailsFragment(it)
                findNavController().navigate(action)
            }
        }

        mBinding.cardInfoStore.btnHide.setOnClickListener {
            mBinding.frameInfoTienda.visibility = View.GONE
        }

        //Si el radio es modificado se ejecuta el bloque
        mStoreViewModel.radiusLD.observe(viewLifecycleOwner, {
            mBinding.tvRatioRange.text = it.toString()
            mBinding.sliderRadius.value = it.toFloat()
            circle?.radius = it * 1000
        })

        //El deslizador modifica el radio
        mBinding.sliderRadius.addOnChangeListener { slider, value, fromUser ->
            val valueRed = Math.round(value.toDouble() * 100.0) / 100.0
            mStoreViewModel.setRadius(valueRed)
        }
    }

    /**
     * Metodo para setear el serachview
     * */
    private fun setupSearchView() {
        val item = mBinding.toolbar.menu.findItem(R.id.action_search)
        val searchView: SearchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                findNavController().navigate(R.id.action_mainStoreFragment_to_searchedStoresFragment)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    /**
     * Metodo para dibujar marcadores
     * @param stores: lista de tiendas
     * */
    private fun drawMarkerStores(stores: List<Store>) {
        if (!::mMap.isInitialized) {
            loadMap()
        } else {
            removeAllMarkers()
            for (store in stores) {
                store.let {
                    val ubi = LatLng(store.location?.latitude!!, store.location?.longitude!!)
                    val mark = mMap.addMarker(
                        MarkerOptions()
                            .position(ubi)
                            .title(store.name)
                            .icon(
                                bitmapDescriptorFromVector(
                                    mBinding.root.context,
                                    R.drawable.ic_store
                                )
                            )
                    )
                    mark?.let {
                        it.tag = store
                        allMarkers.add(it)
                    }
                }
            }
        }


    }

    private fun removeAllMarkers() {
        for (marker in allMarkers) {
            marker.remove()
        }
        allMarkers.clear()
    }

    /**
     * Metodo que solicita las tiendas cercanas al viewmodel y
     * modifica al mismo con el resultado
     * */
    private fun getCloseStore() {
        currentPosition?.let {
            mStoreViewModel.getCloseStores(it.latitude, it.longitude)
                .observe(viewLifecycleOwner, { result ->
                    when (result) {
                        is Resource.Loading -> {
                            mBinding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            if (result.data.isEmpty()) {
                                Toast.makeText(
                                    mBinding.root.context,
                                    getString(R.string.info_not_close_stores),
                                    Toast.LENGTH_LONG
                                ).show();
                            }
                            mBinding.progressBar.visibility = View.GONE
                            mStoreViewModel.updateStores(result.data)
                            //ya no es necesario pintar los marcadores
                            //drawMarkerStores(result.data)
                        }
                        is Resource.Failure -> {
                            Toast.makeText(
                                mBinding.root.context,
                                getString(R.string.info_not_time_service),
                                Toast.LENGTH_LONG
                            ).show();
                            drawMarkerStores(listOf())
                            mBinding.progressBar.visibility = View.GONE
                        }
                    }
                })
        }
    }

    /**
     * Metodo para pedir la ubicacion actual inicial del dispositivo
     * */
    @SuppressLint("MissingPermission")
    fun getInitialCurrentPosition() {
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(mBinding.root.context)

        mFusedLocationClient!!.lastLocation
            .addOnSuccessListener {
                currentPosition = it
                goToCurrentPosition()
            }
            .addOnFailureListener {
                Toast.makeText(mBinding.root.context, "ERROR", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Metodo que pide ultima ubicacion registrada del dispositivo
     * */
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(mBinding.root.context)
        mFusedLocationClient!!.lastLocation.addOnSuccessListener {
            currentPosition = it
            goToCurrentPosition()
        }
    }

    fun goToCurrentPosition() {
        currentPosition?.let {
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latitude, it.longitude),
                    16.5f
                )
            );
            drawCircularArea()
        }

    }

    /**
     * Metodo para cargar info de la tienda en el cardview
     * @param store: contiene los datos de una tienda
     */
    private fun setStoreData(store: Store) {
        mBinding.cardInfoStore.apply {
            tvNameStore.text = store.name
            tvRating.text = store.rating.toString()
            tvAddress.text = store.address
            Glide.with(requireContext())
                .load(store.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imgPhotoStore)

            currentPosition?.let {
                tvDistance.text = Maps.calculateDistanceKm(
                    it.latitude,
                    it.longitude,
                    store.location?.latitude!!,
                    store.location?.longitude!!
                ).toString()
            }

        }
    }

    /**
     * Metodo que dibuja el circulo de area
     * */
    @SuppressLint("NewApi")
    private fun drawCircularArea() {
        circle?.remove()
        val radius = mStoreViewModel.radiusLD.value ?: 0.0
        currentPosition?.let {
            val color = mBinding.root.context.getColor(R.color.primaryColor)
            val colorFill = mBinding.root.context.getColor(R.color.fillArea)
            val circlerOptions = CircleOptions()
                .center(LatLng(it.latitude, it.longitude))
                .radius(radius * 1000)
                .strokeColor(color)
                .fillColor(colorFill)
                .strokeWidth(3.0f)
            circle = mMap.addCircle(circlerOptions)
        }
    }

    /**
     * Metodo para convertir una drawable a mapa de bits para el icono del marcador
     * @param context: contexto
     * @param vectorResId:id del recurso drawable
     * @return un mapa de bits
     * */
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, 128, 128)
            val bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun createPolyline(lat1: Double, lon1: Double, lat2: Double, lon2: Double) {
        val polylineOptions = PolylineOptions()
            .add(LatLng(lat1, lon1))
            .add(LatLng(lat2, lon2))

        val polyline = mMap.addPolyline(polylineOptions)
    }

    /**
     * metodo que se ejecuta cuando el mapa está cargado en el fragment
     * Es decir cuando se llama a cargarMapa()
     * */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        mMap.setOnMarkerClickListener(this)

        enableLocation()

        mStoreViewModel.closeStoresLD.observe(viewLifecycleOwner, {
            drawMarkerStores(it)
        })
    }

    /**
     * Métodos para confirmar los permisos de localización
     */
    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this.requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        if (!::mMap.isInitialized) return
        if (isLocationPermissionGranted()) {
            //si
            mMap.isMyLocationEnabled = true
            getInitialCurrentPosition()

        } else {
            //no
            //pedir permisos
            requesLocationPermission()
        }
    }

    /**
     *Metodo que pide la ubicacion actual del dispositivo
     * */
    private fun requesLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this.requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                requireContext(),
                "ve a ajustes y acepta los permisos",
                Toast.LENGTH_SHORT
            ).show()
        } else {

            /*ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )*/
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_CODE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
                Toast.makeText(mBinding.root.context, "PERMISOS ACEPTADOS", Toast.LENGTH_SHORT)
                    .show()
                getInitialCurrentPosition()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Para activar la localización ve a ajustes y acepta los permisos",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (!::mMap.isInitialized) return
        if (!isLocationPermissionGranted()) {
            mMap.isMyLocationEnabled = false
            Toast.makeText(
                mBinding.root.context,
                "Para activar la localización ve a ajustes y acepta los permisos (onresume)",
                Toast.LENGTH_SHORT
            ).show()
        }

        /*if (!::mMap.isInitialized){
            cargarMapa()
        }*/
    }

    override fun onMyLocationButtonClick(): Boolean {
        //Toast.makeText(mBinding.root.context, "Ahhh mi localizacion", Toast.LENGTH_SHORT).show()
        //calcular posicion actual
        // mover el circulo
        getCurrentLocation()
        //if (::currentPosition.isInitialized) {
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        //
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Toast.makeText(mBinding.root.context, p0.title, Toast.LENGTH_SHORT).show()
        selectedStore = p0.tag as Store
        setStoreData(selectedStore!!)
        mBinding.frameInfoTienda.visibility = View.VISIBLE
        return false
    }
}