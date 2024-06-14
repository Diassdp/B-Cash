package com.example.bcash.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bcash.databinding.ActivityDetailInventoryBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.service.response.data.ProductItem
import java.text.NumberFormat
import java.util.Locale

class DetailInventoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailInventoryBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var data: ProductItem
    private val viewModel: DetailViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupData() // Make sure data is set up before calling insertData
        setupListener()
        insertData()
    }

    private fun setupView(){
        binding = ActivityDetailInventoryBinding.inflate(layoutInflater)
        factory = ViewModelFactory.getInstance(this)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupData() {
        data = intent.getParcelableExtra(EXTRA_DATA) ?: run {
            showToast("No product data found")
            finish()
            return
        }
    }

    private fun setupListener(){
        binding.apply {
            btnRemove.setOnClickListener {
                removeFromInventory()
            }
        }
    }

    private fun removeFromInventory(){
        viewModel.getSession().observe(this@DetailInventoryActivity){
            viewModel.deleteInventory(it.token, it.userId, data.id)
        }

        viewModel.wishlistResponse.observe(this@DetailInventoryActivity){
            if (it.error != true){
                showToast("Product has been removed from wishlist")
            } else {
                showToast("Failed to add product to wishlist")
            }
        }
    }

    private fun insertData() {
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
