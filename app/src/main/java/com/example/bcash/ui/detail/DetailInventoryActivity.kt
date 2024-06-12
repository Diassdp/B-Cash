package com.example.bcash.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bcash.databinding.ActivityDetailBinding
import com.example.bcash.databinding.ActivityDetailInventoryBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.service.response.data.ProductItem
import java.text.NumberFormat
import java.util.Locale

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
            tvPrice.text = data.price?.let { formatPrice(it.toInt()) }
            tvCategory.text = data.category
            tvCondition.text = data.condition
            tvNameUser.text = data.username
            Glide.with(this@DetailInventoryActivity)
                .load(data.photo)
                .fitCenter()
                .into(ivProduct)
        }
    }

    private fun formatPrice(price: Int): String {
        val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
        return "Rp" + format.format(price)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}