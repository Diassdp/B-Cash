package com.example.bcash.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bcash.databinding.ActivityDetailBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.service.response.data.ProductItem
import com.example.bcash.ui.bartertrade.transaction.TransactionActivity
import com.example.bcash.ui.login.LoginActivity
import java.text.NumberFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var data: ProductItem
    private val viewModel: DetailViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupData()
        setupListener()
    }

    private fun setupView(){
        binding = ActivityDetailBinding.inflate(layoutInflater)
        factory = ViewModelFactory.getInstance(this)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupData() {
        data = intent.getParcelableExtra(EXTRA_DATA) ?: return
        insertData()
    }

    private fun setupListener(){
        viewModel.getSession().observe(this@DetailActivity) { session ->
            binding.apply {
                btnTrade.setOnClickListener {
                    if (session.statusLogin) {
                        trade()
                    } else {
                        binding.clLogin.visibility = android.view.View.VISIBLE
                    }
                }

                btnWishlist.setOnClickListener {
                    if (session.statusLogin) {
                        wishlist()
                    } else {
                        binding.clLogin.visibility = android.view.View.VISIBLE
                    }
                }
            }
        }

        binding.clLogin.setOnClickListener {
            binding.clLogin.visibility = android.view.View.GONE
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@DetailActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun trade(){
        val intent = Intent(this@DetailActivity, TransactionActivity::class.java)
        intent.putExtra(TransactionActivity.EXTRA_DATA, data)
        startActivity(intent)
        finish()
    }

    private fun wishlist(){
        viewModel.getSession().observe(this@DetailActivity){
            viewModel.postWishlist(it.token,it.userId, data.id)
        }

        viewModel.wishlistResponse.observe(this@DetailActivity){
            if (it.error != true){
                showToast("Product has been added to wishlist")
            } else {
                showToast("Failed to add product to wishlist")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun insertData() {
        binding.apply {
            tvNamePro.text = data.name
            tvDesc.text = data.description
            tvPrice.text = data.price?.let { formatPrice(it.toInt()) }
            tvCategory.text = data.category
            tvCondition.text = data.condition
            tvNameUser.text = data.username

            Glide.with(this@DetailActivity)
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
