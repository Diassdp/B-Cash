package com.example.bcash.ui.shop

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcash.R
import com.example.bcash.databinding.FragmentShopBinding
import com.example.bcash.model.ViewModelFactory

class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ShopAdapter
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }

    private val viewModel: ShopViewModel by viewModels { factory }

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    private var data: String? = "Barang 1" // Temporary
    private var action: String? = "category"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionData()
        setupView()
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
            binding.searchView.requestFocus()
        }
    }

    private fun setupActionData(){
        arguments?.getString("data")?.let {
            data = it
            Log.e("ShopFragment", "onViewCreated: $data")
            binding.tvCategory.text = it
        }
        arguments?.getString("action")?.let {
            action = it
            Log.e("ShopFragment", "onViewCreated: $action")
            binding.tvCategory.text = it
        }
    }
    private fun setupView(){
        setupAdapter()
        if (action =="category"){
            binding.tvCategory.text = "Category: $data"
            setupProductByCategory(data!!)
        } else if (action == "search"){
            binding.tvCategory.text = "Search: $data"
            setupProductBySearch(data!!)
        }
        setupSearch()
    }

    private fun setupAdapter(){
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = ShopAdapter()
        recyclerView.adapter = adapter
    }

    private fun setupProductByCategory(category: String) {
        viewModel.getProductsByCategory(category).observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
    }

    private fun setupSearch() {
        searchView = binding.searchView
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                setupProductBySearch(query)
                searchView.clearFocus()
                binding.tvCategory.text = "Search: $query"
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
//                setupProductBySearch(newText)
//                binding.tvCategory.text = "Search: $newText"
                return true
            }
        })
    }

    private fun setupProductBySearch(search: String) {
        viewModel.getProductsBySearch(search).observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
