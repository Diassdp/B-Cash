package com.example.bcash.ui.shop

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
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

    private var category: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("category")?.let {
            category = it
            binding.tvCategory.text = it
        }
        setupView()
    }

    private fun setupView(){
        setupAdapter()
        if (category != null) {
            setupProductByCategory(category!!)
        } else {
            // If category is null, don't fetch products by category
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
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                setupProductBySearch(newText)
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
