package com.example.dsm_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dsm_frontend.databinding.ActivityMainBinding
import com.example.dsm_frontend.model.Product
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

        /*val product = Product(
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
        )

        val database = FirebaseDatabase.getInstance()
        val ref = database.reference
        val key = ref.push().key ?: "a"
        ref.child("products").child(key).setValue(product)*/

    }
}