package com.example.bcash.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory
    private var token = ""

    private val mainViewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUser()
        setupView()
        setupNavigation()
        setupToolbar()
    }

    private fun setupUser() {
        factory = ViewModelFactory.getInstance(this)

        mainViewModel.getSession().observe(this@MainActivity) {
            token = it.token
            Log.d(TAG, "Token: $token")
            Log.d(TAG, "statusLogin: ${it.statusLogin}")
            if (!it.statusLogin) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setupView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
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

        navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_dashboard,
                R.id.navigation_shop,
                R.id.navigation_bartertrade,
                R.id.navigation_favorite,
                R.id.navigation_profile -> {
                    menuItem.isChecked = true
                    navigateTo(menuItem.itemId)
                    true
                }
                else -> false
            }
        }
    }
    
    private fun setupToolbar() {
        findViewById<ImageView>(R.id.iv_logo).setOnClickListener {
            navigateTo(R.id.navigation_dashboard)
        }

        findViewById<TextView>(R.id.tv_logo).setOnClickListener {
            navigateTo(R.id.navigation_dashboard)
        }

        findViewById<ImageView>(R.id.iv_account).setOnClickListener {
            navigateTo(R.id.navigation_profile)
        }

        findViewById<ImageView>(R.id.iv_inventory).setOnClickListener {
            navigateTo(R.id.navigation_inventory)
        }

        findViewById<ImageView>(R.id.iv_favorite).setOnClickListener {
            navigateTo(R.id.navigation_favorite)
        }
    }

    private fun navigateTo(fragmentId: Int) {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.popBackStack()
        navController.navigate(fragmentId)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
