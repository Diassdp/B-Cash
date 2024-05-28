package com.example.bcash.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcash.R
import com.example.bcash.dummy.PopularAdapter
import com.example.bcash.dummy.PopularItem

class InventoryFragment : Fragment() {

    private lateinit var popularAdapter: PopularAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)

        setupRecyclerView(view)

        return view
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        popularAdapter = PopularAdapter(listOf(
            PopularItem(R.drawable.dummy_img, "Product 1", "$20"),
            PopularItem(R.drawable.dummy_img, "Product 2", "$25"),
            PopularItem(R.drawable.dummy_img, "Product 2", "$25"),
            PopularItem(R.drawable.dummy_img, "Product 2", "$25"),
            // Add more items as needed
        ))
        recyclerView.adapter = popularAdapter
    }
}