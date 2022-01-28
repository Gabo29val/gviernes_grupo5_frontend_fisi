package com.example.dsm_frontend.storeModule

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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.FragmentMainStoreBinding
import com.example.dsm_frontend.model.Store
import com.example.dsm_frontend.presentation.StoreViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import java.math.RoundingMode


class MainStoreFragment : Fragment(R.layout.fragment_main_store), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var mStoreViewModel: StoreViewModel

    private lateinit var mBinding: FragmentMainStoreBinding
    private lateinit var mMap: GoogleMap
    private lateinit var currentPosition: LatLng
    private var circle: Circle? = null
    private var radius: Double = 0.10

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentMainStoreBinding.bind(view)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
        setupComponents()
        setupSearchView()
    }

    private fun setupViewModel() {
        mStoreViewModel = ViewModelProvider(requireActivity()).get(StoreViewModel::class.java)

        //getCloseStore()

        /*mStoreViewModel.getStores().observe(requireActivity(), {
            drawMarkerStores(it)
        })*/
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

                    /*currentPosition.let {
                        createPolyline(
                            mark?.position!!.latitude,
                            mark.position.longitude,
                            currentPosition!!.latitude,
                            currentPosition!!.longitude
                        )
                    }*/

                }
            }
        }
    }

    private fun setupComponents() {
        mBinding.btnAplicar.setOnClickListener {
            //aqui hace la consulta con el nuevo radio
            getCloseStore()
        }

        mBinding.tvRatioRange.text = radius.toString()

        mBinding.sliderRadius.value = radius.toFloat()

        mBinding.sliderRadius.addOnChangeListener { slider, value, fromUser ->
            val valueRed = Math.round(value.toDouble() * 100.0) / 100.0
            mBinding.tvRatioRange.text = valueRed.toString()
            //value.toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString()
            radius = valueRed
            circle?.radius = radius * 1000
        }
    }

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
        if (::currentPosition.isInitialized) {
            currentPosition.let {
                val radiusMillas = Math.round(((radius) * 0.62137) * 100.0) / 100.0
                /*Log.d(
                    "CONSULTA",
                    "lat ${it!!.latitude} lon${it!!.longitude} radiusMillas: $radiusMillas"
                )*/
                val d = it
                val a = it!!.latitude
                val b = it.longitude
                val c = radiusMillas
                mStoreViewModel.getCloseStores(it!!.latitude, it.longitude, radiusMillas)
                    .observe(requireActivity(), {
                        drawMarkerStores(it)
                    })
            }
        }
    }


    //metodo que pide ultima ubicacion registrada del dispositivo
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mBinding.root.context)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            try {
                drawCircularArea(it.latitude, it.longitude, radius)
                currentPosition = LatLng(it.latitude, it.longitude)
                Log.d(
                    "CURRENT",
                    "lat ${it.latitude} lon${it.longitude}"
                )
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.latitude, it.longitude),
                        16.5f
                    )
                );
            } catch (e: Exception) {
                Log.e("error", e.message!!)
            }
        }
    }

    //metodo que se ejecuta cuando el mapa está cargado
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        mMap.setOnMarkerClickListener(this)

        enableLocation()
        getCurrentLocation()
        //generarMarcadores()
        getCloseStore()
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
        }
    }

    //Metodo que dibuja el circulo de area
    @SuppressLint("NewApi")
    private fun drawCircularArea(latitude: Double, longitude: Double, radius: Double) {
        //try {
        val color = mBinding.root.context.getColor(R.color.primaryColor)
        val colorFill = mBinding.root.context.getColor(R.color.fillArea)
        val circlerOptions = CircleOptions()
            .center(LatLng(latitude, longitude))
            .radius(radius * 1000)
            .strokeColor(color)
            .fillColor(colorFill)
            .strokeWidth(3.0f)
        circle = mMap.addCircle(circlerOptions)
        /*} catch (e: Exception) {
            e.message?.let { Log.e("Error", it) }
        }*/

    }

    //Metodo para convertir una drawable a mapa de bits para el icono del marcador
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            //setBounds(0, 0, intrinsicWidth, intrinsicHeight)
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
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        //
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Toast.makeText(mBinding.root.context, p0.title, Toast.LENGTH_SHORT).show()
        setStoreData(p0.tag as Store)
        mBinding.cardInfoStore.card.visibility = View.VISIBLE
        return false
    }
}