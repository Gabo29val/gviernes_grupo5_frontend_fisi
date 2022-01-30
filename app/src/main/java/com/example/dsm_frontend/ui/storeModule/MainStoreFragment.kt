package com.example.dsm_frontend.ui.storeModule

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.api.Maps
import com.example.dsm_frontend.api.RetrofitClient
import com.example.dsm_frontend.core.Resource
import com.example.dsm_frontend.data.StoreDataSource
import com.example.dsm_frontend.databinding.FragmentMainStoreBinding
import com.example.dsm_frontend.data.model.Store
import com.example.dsm_frontend.presentation.StoreViewModel
import com.example.dsm_frontend.presentation.StoreViewModelFactory
import com.example.dsm_frontend.repository.StoreRepositoryImpl
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory


class MainStoreFragment : Fragment(R.layout.fragment_main_store), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMarkerClickListener {

    private val mStoreViewModel by viewModels<StoreViewModel> {
        StoreViewModelFactory(
            StoreRepositoryImpl(
                StoreDataSource(
                    RetrofitClient.apiService
                )
            )
        )
    }

    private lateinit var mBinding: FragmentMainStoreBinding
    private lateinit var mMap: GoogleMap
    private var currentPosition: Location? = null
    private var circle: Circle? = null
    private var radius: Double = 0.10 //EN KM

    private var mFusedLocationClient: FusedLocationProviderClient? = null

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    // Location classes
    private var mTrackingLocation = true
    private var myLocation: Location? = null
    private var mLocationCallback: LocationCallback? = null
    private val TRACKING_LOCATION_KEY = "tracking_location"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentMainStoreBinding.bind(view)
        // Restore the state if the activity is recreated.
        if (savedInstanceState != null) {
            mTrackingLocation = savedInstanceState.getBoolean(TRACKING_LOCATION_KEY)
        }

        getInitialCurrentPosition()
        setupComponents()
        setupSearchView()
    }

    @SuppressLint("MissingPermission")
    fun getInitialCurrentPosition() {
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(mBinding.root.context)
        mFusedLocationClient!!.lastLocation.addOnSuccessListener {

            currentPosition = it
            cargarMapa()
            //drawCircularArea(it.latitude, it.longitude, radius)
            //currentPosition = LatLng(it.latitude, it.longitude)
        }
    }

    //Metodo para cargar el mapa en el fragmento
    fun cargarMapa() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun drawMarkerStores(stores: List<Store>) {
        if (::mMap.isInitialized) {
            mMap.clear()
            currentPosition.let {
                currentPosition?.let { it1 ->
                    drawCircularArea(
                        it1.latitude,
                        currentPosition!!.longitude,
                        radius
                    )
                }
            }

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
                    mark?.tag = store
                }
            }
        }
    }

    //metodo para inicializar valores de componentes
    private fun setupComponents() {
        mBinding.frameInfoTienda.visibility = View.GONE

        mBinding.btnAplicar.setOnClickListener {
            //aqui hace la consulta con el nuevo radio
            getCloseStore()
        }

        mBinding.tvRatioRange.text = radius.toString()

        mBinding.sliderRadius.value = radius.toFloat()

        mBinding.sliderRadius.addOnChangeListener { slider, value, fromUser ->
            val valueRed = Math.round(value.toDouble() * 100.0) / 100.0
            mBinding.tvRatioRange.text = valueRed.toString()
            radius = valueRed
            circle?.radius = radius * 1000
        }
    }

    //metodo para setear el serachview
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

    //metodo para solicitar tiendas cercanas
    private fun getCloseStore() {
       // if (::currentPosition.isInitialized) {
        currentPosition?.let {
            currentPosition.let {
                val radiusMillas = Math.round(((radius) * 0.62137) * 100.0) / 100.0
                mStoreViewModel.getCloseStores(it!!.latitude, it.longitude, radiusMillas)
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
                                drawMarkerStores(result.data)
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
    }


    //metodo que pide ultima ubicacion registrada del dispositivo
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(mBinding.root.context)
        mFusedLocationClient!!.lastLocation.addOnSuccessListener {

            drawCircularArea(it.latitude, it.longitude, radius)
            currentPosition = it

            Log.d(
                "CURRENT",
                "lat ${it.latitude} lon${it.longitude}"
            )
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latitude, it.longitude),
                    16.5f
                )
            );


        }
    }

    //metodo que se ejecuta cuando el mapa está cargado
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        mMap.setOnMarkerClickListener(this)

        currentPosition?.let {
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latitude, it.longitude),
                    16.5f
                )
            );
            drawCircularArea(it.latitude, it.longitude, radius)
        }

        enableLocation()
        //getCurrentLocation()
        //generarMarcadores()
        //getCloseStore()
    }

    //metodo para cargar info de la tienda en el cardview
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

    //Metodo que dibuja el circulo de area
    @SuppressLint("NewApi")
    private fun drawCircularArea(latitude: Double, longitude: Double, radius: Double) {
        val color = mBinding.root.context.getColor(R.color.primaryColor)
        val colorFill = mBinding.root.context.getColor(R.color.fillArea)
        val circlerOptions = CircleOptions()
            .center(LatLng(latitude, longitude))
            .radius(radius * 1000)
            .strokeColor(color)
            .fillColor(colorFill)
            .strokeWidth(3.0f)
        circle = mMap.addCircle(circlerOptions)
    }

    //Metodo para convertir una drawable a mapa de bits para el icono del marcador
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

        } else {
            //no
            //pedir permisos
            requesLocationPermission()
        }
    }

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
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
        if (!isLocationPermissionGranted()) {
            mMap.isMyLocationEnabled = false
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        //Toast.makeText(mBinding.root.context, "Ahhh mi localizacion", Toast.LENGTH_SHORT).show()
        //calcular posicion actual
        // mover el circulo
        getCurrentLocation()
        //if (::currentPosition.isInitialized) {
        currentPosition?.let {
            circle?.remove()
        }
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        //
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Toast.makeText(mBinding.root.context, p0.title, Toast.LENGTH_SHORT).show()
        setStoreData(p0.tag as Store)
        mBinding.frameInfoTienda.visibility = View.VISIBLE
        return false
    }
}