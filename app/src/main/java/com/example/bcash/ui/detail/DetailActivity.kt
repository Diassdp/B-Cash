package com.example.bcash.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bcash.databinding.ActivityDetailBinding
import com.example.bcash.service.response.ProductItem
import com.example.bcash.ui.bartertrade.transaction.TransactionActivity

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        insertData()
        setupListener()
    }

    private fun setupListener(){
        binding.apply {
            btnTrade.setOnClickListener {
                trade()
            }

            btnWishlist.setOnClickListener {
                wishlist()
            }
        }
    }

    private fun trade(){
        val data = intent.getParcelableExtra<ProductItem>(EXTRA_DATA) as ProductItem
        val intent = Intent(this@DetailActivity, TransactionActivity::class.java)
        intent.putExtra(TransactionActivity.EXTRA_DATA, data)
        startActivity(intent)
    }

    private fun wishlist(){

    }

    private fun setupView(){
        binding = ActivityDetailBinding.inflate(layoutInflater)
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


            Glide.with(this@DetailActivity)
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