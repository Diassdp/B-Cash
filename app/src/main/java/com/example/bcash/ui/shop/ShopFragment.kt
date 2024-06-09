package com.example.bcash.ui.shop

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcash.R
import com.example.bcash.databinding.FragmentShopBinding
import com.example.bcash.dummy.PopularAdapter
import com.example.bcash.dummy.PopularItem
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.service.response.data.ProductItem

class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ShopAdapter
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }

    private val viewModel: ShopViewModel by viewModels { factory }

    private lateinit var recyclerView: RecyclerView
    private lateinit var popularAdapter: PopularAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val category = arguments?.getString("category")
        binding.tvCategory.text = category
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView(){
        setupAdapter()
//        dummyView()
    }

    private fun setupAdapter(){
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = ShopAdapter()
        recyclerView.adapter = adapter
    }

        private fun setupViewModel(category: String) {
            viewModel.getProductsByCategory(category).observe(viewLifecycleOwner) { pagingData ->
                adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }

        }
//    private fun dummyView(){
//        adapter.submitData(viewLifecycleOwner.lifecycle, PagingData.from(dummyData))
//    }

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
//    val dummyData = listOf(
//        ProductItem("1", "Product 1", "https://images-ext-1.discordapp.net/external/8YwJuF5LQA0QykOKHTHehSNHDbSnenl0QWtyVNszhe8/https/img.lazcdn.com/g/p/18de5a0582d6fbfbce7b3dffec049e6c.jpg_720x720q80.jpg_.webp?format=webp&width=676&height=676", "dummy_image_url","100K","baju","baru","Bob" ),
//        ProductItem("2", "Product 2", "https://images-ext-1.discordapp.net/external/UtxCvcjDcuhkd4iWogrAkZdxPITsyGppglQdRBc7nUo/https/static4.depositphotos.com/1007248/289/i/450/depositphotos_2891812-stock-photo-plastic-dummy.jpg?format=webp", "dummy_image_url","200K","baju","bekas","Builder"),
//        ProductItem("3", "Product 3", "https://images-ext-1.discordapp.net/external/UtxCvcjDcuhkd4iWogrAkZdxPITsyGppglQdRBc7nUo/https/static4.depositphotos.com/1007248/289/i/450/depositphotos_2891812-stock-photo-plastic-dummy.jpg?format=webp", "dummy_image_url","300K","elektronik","baru","opak"),
//        ProductItem("4", "Product 4", "https://images-ext-1.discordapp.net/external/UtxCvcjDcuhkd4iWogrAkZdxPITsyGppglQdRBc7nUo/https/static4.depositphotos.com/1007248/289/i/450/depositphotos_2891812-stock-photo-plastic-dummy.jpg?format=webp", "dummy_image_url","400K","elektronik","baru","yonathan"),
//    )
}
