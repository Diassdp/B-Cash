package com.example.bcash.ui.inventory

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcash.databinding.FragmentInventoryBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.service.response.ProductItem

class InventoryFragment : Fragment() {
    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: InventoryAdapter
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }
    private val viewModel: InventoryViewModel by viewModels { factory }
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        setupView()
    }

    private fun setupView() {
        setupAdapter()
        dummyView()
        countInventoryItems()
//        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.getSession().observe(viewLifecycleOwner, Observer { session ->
            session?.let {
                viewModel.getInventory(it.token, it.userId)
            }
        })

        viewModel.inventoryResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.error != true) {
                response.listProduct.let { list ->
                    adapter.submitData(viewLifecycleOwner.lifecycle, PagingData.from(list))
                }
            } else {
                Log.e("InventoryFragment","Error fetching inventory: ${response.message}")
                Toast.makeText(requireContext(), "Error fetching inventory", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun dummyView(){
        adapter.submitData(viewLifecycleOwner.lifecycle, PagingData.from(dummyData))
    }

    private fun setupAdapter() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = InventoryAdapter()
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun countInventoryItems() {
        val inventoryItemsCount = adapter.itemCount
        binding.tvFavCount.text = "Inventory Items: $inventoryItemsCount"
    }

    val dummyData = listOf(
        ProductItem("1", "Product 1", "https://images-ext-1.discordapp.net/external/8YwJuF5LQA0QykOKHTHehSNHDbSnenl0QWtyVNszhe8/https/img.lazcdn.com/g/p/18de5a0582d6fbfbce7b3dffec049e6c.jpg_720x720q80.jpg_.webp?format=webp&width=676&height=676", "dummy_image_url","100K","baju","baru","Bob" ),
        ProductItem("2", "Product 2", "https://images-ext-1.discordapp.net/external/UtxCvcjDcuhkd4iWogrAkZdxPITsyGppglQdRBc7nUo/https/static4.depositphotos.com/1007248/289/i/450/depositphotos_2891812-stock-photo-plastic-dummy.jpg?format=webp", "dummy_image_url","200K","baju","bekas","Builder"),
        ProductItem("3", "Product 3", "https://images-ext-1.discordapp.net/external/UtxCvcjDcuhkd4iWogrAkZdxPITsyGppglQdRBc7nUo/https/static4.depositphotos.com/1007248/289/i/450/depositphotos_2891812-stock-photo-plastic-dummy.jpg?format=webp", "dummy_image_url","300K","elektronik","baru","opak"),
        ProductItem("4", "Product 4", "https://images-ext-1.discordapp.net/external/UtxCvcjDcuhkd4iWogrAkZdxPITsyGppglQdRBc7nUo/https/static4.depositphotos.com/1007248/289/i/450/depositphotos_2891812-stock-photo-plastic-dummy.jpg?format=webp", "dummy_image_url","400K","elektronik","baru","yonathan"),
    )
}