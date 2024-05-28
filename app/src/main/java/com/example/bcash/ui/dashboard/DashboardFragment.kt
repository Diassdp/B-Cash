package com.example.bcash.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ViewFlipper
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcash.R
import com.example.bcash.dummy.PopularAdapter
import com.example.bcash.dummy.PopularItem

class DashboardFragment : Fragment() {

    private lateinit var popularAdapter: PopularAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setupViewFlipper(view)
        setupCategoryButtons(view)
        setupRecyclerView(view)

        return view
    }

    private fun setupViewFlipper(view: View) {
        val viewFlipper = view.findViewById<ViewFlipper>(R.id.viewFlipper)
        viewFlipper.setInAnimation(context, R.anim.slide_in_left)
        viewFlipper.setOutAnimation(context, R.anim.slide_in_right)
        viewFlipper.flipInterval = 8000
        viewFlipper.startFlipping()
    }

    private fun setupCategoryButtons(view: View) {
        // Example for one button
        val btnBarang1 = view.findViewById<ImageButton>(R.id.btn_barang1)
        btnBarang1.setOnClickListener {
            // Implement your click handling logic
        }
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
