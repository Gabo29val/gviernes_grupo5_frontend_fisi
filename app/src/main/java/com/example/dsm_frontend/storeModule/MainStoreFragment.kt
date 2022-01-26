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
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.databinding.FragmentMainStoreBinding
import com.example.dsm_frontend.model.Store
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
    GoogleMap.OnMyLocationClickListener {

    private lateinit var mBinding: FragmentMainStoreBinding
    private lateinit var mMap: GoogleMap
    private var circle: Circle? = null
    private var radius: Double = 0.25

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentMainStoreBinding.bind(view)

        setupComponents()
        setupSearchView()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        generarArea()
    }

    @SuppressLint("MissingPermission")
    private fun generarArea() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mBinding.root.context)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            Toast.makeText(
                mBinding.root.context,
                "Lat: ${it?.latitude}, Lon: ${it?.longitude} radius: $radius",
                Toast.LENGTH_SHORT
            ).show()
            try {
                if (it.latitude != null && it.longitude != null) {
                    generarArea(it.latitude, it.longitude, radius)
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(it.latitude, it.longitude),
                            16.5f
                        )
                    );
                }
            } catch (e: Exception) {
                Log.e("errorrrrrr", e.message!!)
            }

            //mMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)), )
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

    private fun setupComponents() {
        mBinding.btnAplicar.setOnClickListener {
            //aqui hace la consulta con el nuevo radio
        }

        mBinding.tvRatioRange.text =
            radius.toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString()

        mBinding.sliderRadius.value = radius.toBigDecimal().setScale(2, RoundingMode.UP).toFloat()

        mBinding.sliderRadius.addOnChangeListener { slider, value, fromUser ->
            mBinding.tvRatioRange.text =
                value.toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString()
            radius = value.toDouble()
            circle?.radius = radius * 1000
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        generarMarcadores()
        //createPolyline()

        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        mMap.setOnMarkerClickListener {
            Toast.makeText(mBinding.root.context, it.title, Toast.LENGTH_SHORT).show()
            //mBinding.cardInfoStore.tvNameStore.text = it.title
            setStoreData(it.tag as Store)
            mBinding.cardInfoStore.card.visibility = View.VISIBLE
            false
        }


        enableLocation()
    }

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

    @SuppressLint("NewApi")
    private fun generarArea(latitude: Double, longitude: Double, radius: Double) {
        val color = requireContext().getColor(R.color.primaryColor)
        val colorFill = requireContext().getColor(R.color.fillArea)
        val circlerOptions = CircleOptions()
            .center(LatLng(latitude, longitude))
            .radius(radius * 1000)
            .strokeColor(color)
            .fillColor(colorFill)
            .strokeWidth(3.0f)
        circle = mMap.addCircle(circlerOptions)
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            //setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            setBounds(0, 0, 128, 128)
            val bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun generarMarcadores() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("stores")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (storeSnapshot in snapshot.children) {
                        val store = storeSnapshot.getValue(Store::class.java)
                        Log.d("stores", "store: " + store.toString())
                        store?.location?.let {
                            val ubi = LatLng(it.latitude!!, it.longitude!!)
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

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

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

    private fun createPolyline() {
        val polylineOptions = PolylineOptions()
            .add(LatLng(-11.976423, -76.907949))
            .add(LatLng(-11.976376, -76.908973))

        val polyline = mMap.addPolyline(polylineOptions)
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
}