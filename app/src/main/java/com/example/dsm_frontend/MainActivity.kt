package com.example.dsm_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dsm_frontend.databinding.ActivityMainBinding
import com.example.dsm_frontend.data.model.Location
import com.example.dsm_frontend.data.model.Product
import com.example.dsm_frontend.data.model.Specification
import com.example.dsm_frontend.data.model.Store
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navView = mBinding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        //generarProductosIniciales()
        //generarTiendasIniciales()
    }

    fun generarProductosIniciales() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.reference

        for (i in 1..10) {
            val product = Product(
                name = "Red label $i",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcohólica a base de malta fermentada de cereales como cebada, trigo, centeno y maíz, que se destila y añeja en barriles de madera tradicionalmente de roble blanco. Este último proceso dura por lo menos tres años para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus orígenes en Irlanda y Escocia y en la actualidad se disfruta en muchos países a nivel mundial.",
                stock = 10,
                specifications = listOf(
                    Specification("Presentación", "Botella"),
                    Specification("Composición", "Grano y malta"),
                    Specification("Proceso de añejamiento", "No declarada"),
                    Specification("Volumen neto", "750ml"),
                )
            )
            val key = ref.push().key ?: "a"
            ref.child("products").child(key).setValue(product)
        }
    }

    fun generarTiendasIniciales() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.reference

        for (i in 1..2) {
            val store = Store(
                name = "Minimarket $i",
                photoUrl = "https://emprendedorestv.pe/wp-content/uploads/2015/09/7953678590_9b2586cacd_b.jpg",
                address = "C. Amador Merino Reyna 465, San Isidro 15046",
                rating = 5.0,
                location = Location(-12.0921183, -77.0262412)
            )
            val key = ref.push().key ?: "b"
            ref.child("stores").child(key).setValue(store)
        }
    }
}