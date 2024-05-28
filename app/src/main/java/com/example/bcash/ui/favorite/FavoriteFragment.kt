package com.example.bcash.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcash.R
import com.example.bcash.dummy.PopularAdapter
import com.example.bcash.dummy.PopularItem

class FavoriteFragment : Fragment() {

    private lateinit var popularAdapter: PopularAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

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
