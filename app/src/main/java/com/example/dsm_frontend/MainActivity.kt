package com.example.dsm_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dsm_frontend.carritoModule.MainCarFragment
import com.example.dsm_frontend.databinding.ActivityMainBinding
import com.example.dsm_frontend.searchModule.search.MainSearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navView = mBinding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

    }
}