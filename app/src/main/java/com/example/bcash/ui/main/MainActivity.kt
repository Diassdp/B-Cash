package com.example.bcash.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.bcash.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.nav_view)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    navController.navigate(R.id.navigation_dashboard)
                    true
                }
                R.id.navigation_shop -> {
                    navController.navigate(R.id.navigation_shop)
                    true
                }
                R.id.navigation_bartertrade -> {
                    navController.navigate(R.id.navigation_bartertrade)
                    true
                }
                R.id.navigation_favorite -> {
                    navController.navigate(R.id.navigation_favorite)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile)
                    true
                }
                else -> false
            }.also {
                if (navController.currentDestination?.id != R.id.navigation_inventory) {
                    navController.popBackStack(R.id.navigation_inventory, true)
                }
            }
        }
    }
}
