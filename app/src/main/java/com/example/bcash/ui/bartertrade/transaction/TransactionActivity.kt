package com.example.bcash.ui.bartertrade.transaction

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.bcash.R
import com.example.bcash.databinding.ActivityTransactionBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.service.response.data.ProductItem
import com.example.bcash.ui.bartertrade.endportal.EndPortalActivity
import com.example.bcash.ui.inventory.inventransaction.InventoryTransactionAdapter
import com.example.bcash.ui.inventory.inventransaction.InventoryTransactionFragment

class TransactionActivity : AppCompatActivity(), InventoryTransactionAdapter.ItemClickListener {
    private lateinit var binding: ActivityTransactionBinding
    private lateinit var factory: ViewModelFactory
    private var dataSeller: ProductItem? = null
    private var dataBuyer: ProductItem? = null
    private val viewModel: TransactionViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        setupListener()
        supportFragmentManager.setFragmentResultListener("requestKey", this) { key, bundle ->
            val result = bundle.getParcelable<ProductItem>("selectedProduct")
            handleFragmentResult(result)
        }
    }

    private fun setupListener() {
        binding.apply {
            clBuyer.setOnClickListener {
                openInventoryFragment()
            }
            btnTransaction.setOnClickListener {
                if (binding.checkboxSeller.isChecked.not() || binding.checkboxBuyer.isChecked.not()) {
                    showToast("Data Incomplete, Please Check Again")
                } else {
                    actionTransaction()
                }
            }
        }
    }

    private fun openInventoryFragment() {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, InventoryTransactionFragment())
            addToBackStack(null)  // Optional: adds transaction to the back stack
        }
    }

    private fun setupView() {
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        factory = ViewModelFactory.getInstance(this)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupSeller()
    }

    private fun setupBuyer(data: ProductItem?) {
        data?.let {
            binding.apply {
                tvBuyerItem.text = it.name
                tvBuyerPriceItem.text = it.price
                tvUserBuyer.text = it.username
                clChooseItem.visibility = View.GONE

                Glide.with(this@TransactionActivity)
                    .load(it.photo)
                    .fitCenter()
                    .into(ivBuyerItem)
            }
        }
    }

    private fun setupSeller() {
        dataSeller = intent.getParcelableExtra<ProductItem>(EXTRA_DATA)
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

    private fun actionTransaction() {
        viewModel.getSession().observe(this@TransactionActivity) { session ->
            session?.let {
                val sellerId = dataSeller?.id
                val buyerId = dataBuyer?.id
                val sellerUsername = dataSeller?.username
                val buyerUsername = dataBuyer?.username

                if (sellerId != null && buyerId != null && sellerUsername != null && buyerUsername != null) {
                    viewModel.createTradeRequest(it.token, sellerId, buyerId, sellerUsername, buyerUsername)
                } else {
                    showToast("Data Incomplete, Please Check Again")
                }
            }
        }

        viewModel.tradeRequestReponse.observe(this@TransactionActivity) { response ->
            if (!response.error) {
                showToast("Trade successfully")
                moveToEndPortal()
            } else {
                showToast("Failed to Trade")
            }
        }
    }

    private fun moveToEndPortal() {
        val intent = Intent(this@TransactionActivity, EndPortalActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleFragmentResult(result: ProductItem?) {
        dataBuyer = result
        setupBuyer(dataBuyer)
    }

    override fun onItemClick(data: ProductItem) {
        openInventoryFragment(data)
    }

    private fun openInventoryFragment(data: ProductItem) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, InventoryTransactionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("selectedProduct", data)
                }
            })
            addToBackStack(null)  // Optional: adds transaction to the back stack
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}