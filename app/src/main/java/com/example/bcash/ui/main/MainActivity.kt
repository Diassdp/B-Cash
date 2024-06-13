package com.example.bcash.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.bcash.R
import com.example.bcash.databinding.ActivityMainBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

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
        handleIntentExtras()
    }

    private fun setupUser() {
        factory = ViewModelFactory.getInstance(this)

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
            menuItem.isChecked = true
            navigateTo(menuItem.itemId)
            true
        }
    }

    private fun setupToolbar() {
        mainViewModel.getSession().observe(this@MainActivity) { session ->
            findViewById<ImageView>(R.id.iv_logo).setOnClickListener {
                navigateTo(R.id.navigation_dashboard)
            }

            findViewById<TextView>(R.id.tv_logo).setOnClickListener {
                navigateTo(R.id.navigation_dashboard)
            }

            findViewById<ImageView>(R.id.iv_account).setOnClickListener {
                if (session.statusLogin) {
                    navigateTo(R.id.navigation_profile)
                } else {
                    moveToLogin()
                }
            }

            findViewById<ImageView>(R.id.iv_inventory).setOnClickListener {
                if (session.statusLogin) {
                    navigateTo(R.id.navigation_inventory)

                } else {
                    moveToLogin()
                }
            }

            findViewById<ImageView>(R.id.iv_favorite).setOnClickListener {
                if (session.statusLogin) {
                    navigateTo(R.id.navigation_favorite)
                } else {
                    moveToLogin()
                }
            }
        }
    }

    private fun moveToLogin() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }


    private fun navigateTo(fragmentId: Int) {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.popBackStack()
        navController.navigate(fragmentId)
    }

    private fun handleIntentExtras() {
        if (intent.hasExtra(EXTRA_FRAGMENT_ID)) {
            val fragmentId = intent.getIntExtra(EXTRA_FRAGMENT_ID, 0)
            navigateTo(fragmentId)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        const val EXTRA_FRAGMENT_ID = "fragment_id"
    }
}
