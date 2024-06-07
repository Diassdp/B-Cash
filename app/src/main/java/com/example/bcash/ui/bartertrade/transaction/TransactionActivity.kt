package com.example.bcash.ui.bartertrade.transaction

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.example.bcash.R
import com.example.bcash.databinding.ActivityTransactionBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.service.response.ProductItem

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding
    private lateinit var factory: ViewModelFactory

    private val viewModel: TransactionViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        setupViewModel()
        supportFragmentManager.setFragmentResultListener("requestKey", this) { key, bundle ->
            val result = bundle.getString("bundleKey")
            // Handle the result
            handleFragmentResult(result)
        }
    }

    private fun setupListener(){
        binding.apply {
            clBuyer.setOnClickListener {
                //
            }
        }
    }

    private fun setupView(){
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupSeller()
    }

    private fun setupTrade() {
        // Implement trade setup logic here
    }

    private fun setupBuyer(){
        // Implement buyer setup logic here
    }

    private fun setupSeller(){
        val dataSeller = intent.getParcelableExtra<ProductItem>(EXTRA_DATA) as? ProductItem
        dataSeller?.let {
            binding.apply {
                tvSellerItem.text = it.name
                tvSellerPriceItem.text = it.price
                tvUserSeller.text = it.username
                checkboxSeller.isChecked = true

                Glide.with(this@TransactionActivity)
                    .load(it.photo)
                    .fitCenter()
                    .into(ivSellerItem)
            }
        }
    }

    private fun setupViewModel(){
        // Implement ViewModel setup logic here
    }

    private fun handleFragmentResult(result: String?) {
        // Implement your logic to handle the result from the fragment
        if (result != null) {
            // Use the result without directly affecting the existing data in the activity
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
