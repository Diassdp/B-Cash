package com.example.bcash.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.bcash.R
import com.example.bcash.databinding.ActivityMainBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.ui.login.LoginActivity
import com.example.bcash.ui.login.LoginViewModel.Companion.TAG

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory
    private var token = ""
    private val mainViewModel: MainViewModel by viewModels { factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
//        setupUser()
        setupNavigation()
    }

    private fun setupUser() {
        mainViewModel.getSession().observe(this@MainActivity) {
            token = it.token
            Log.d(TAG, "Token: $token")
            if (!it.statusLogin) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setupView(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        factory = ViewModelFactory.getInstance(this)
        setContentView(binding.root)
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_shop,
                R.id.navigation_bartertrade,
                R.id.navigation_favorite,
                R.id.navigation_profile
            )
        )

        navView.setupWithNavController(navController)
    }
}
