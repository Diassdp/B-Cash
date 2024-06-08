package com.example.bcash.ui.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.bcash.R
import com.example.bcash.databinding.ActivityDetailBinding
import com.example.bcash.databinding.ActivityDetailInventoryBinding
import com.example.bcash.service.response.ProductItem

class DetailInventoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailInventoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        insertData()
    }

    private fun setupView(){
        binding = ActivityDetailInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun insertData() {
        val data = intent.getParcelableExtra<ProductItem>(EXTRA_DATA) as ProductItem
        binding.apply {
            tvNamePro.text = data.name
            tvDesc.text = data.description
            tvPrice.text = data.price
            tvCategory.text = data.category
            tvCondition.text = data.condition
            tvNameUser.text = data.username
            Glide.with(this@DetailInventoryActivity)
                .load(data.photo)
                .fitCenter()
                .into(ivProduct)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}