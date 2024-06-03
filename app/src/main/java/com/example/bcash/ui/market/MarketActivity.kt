package com.example.bcash.ui.market

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcash.R
import com.example.bcash.dummy.PopularAdapter
import com.example.bcash.dummy.PopularItem

class MarketActivity : AppCompatActivity() {

    private lateinit var popularAdapter: PopularAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        popularAdapter = PopularAdapter(listOf(
            PopularItem(R.drawable.dummy_img, "Product 1", "$20"),
            PopularItem(R.drawable.dummy_img, "Product 2", "$25"),
            PopularItem(R.drawable.dummy_img, "Product 3", "$30"),
            PopularItem(R.drawable.dummy_img, "Product 4", "$35"),
            // Add more items as needed
        ))
        recyclerView.adapter = popularAdapter
    }
}