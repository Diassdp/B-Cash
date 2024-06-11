package com.example.bcash.ui.favorite

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcash.databinding.FragmentFavoriteBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.service.response.data.ProductItem

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoriteAdapter
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(requireContext()) }
    private val viewModel: FavoriteViewModel by viewModels { factory }
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        setupView()
    }

    private fun setupView(){
        setupAdapter()
        setupViewModel()
        countFavoriteItems()
    }

    private fun setupViewModel() {
        viewModel.getSession().observe(viewLifecycleOwner, Observer { session ->
            session?.let {
                viewModel.getWishlist(it.token, it.userId)
            }
        })

        viewModel.wishlistResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.error == true) {
                response.wishlist.let { list ->
                    if (list != null){
                        Log.e("InventoryFragment","Success fetching inventory")
                        adapter.submitData(viewLifecycleOwner.lifecycle, PagingData.from(list))
                    } else {
                        Log.e("InventoryFragment","Error fetching inventory: ${response.message}")
                        Toast.makeText(requireContext(), "Error fetching inventory", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Log.e("InventoryFragment","Error fetching inventory: ${response.message}")
                Toast.makeText(requireContext(), "Error fetching inventory", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupAdapter() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = FavoriteAdapter()
        recyclerView.adapter = adapter
    }

    private fun countFavoriteItems() {
        val favoriteItemsCount = adapter.itemCount
        binding.tvFavCount.text = "Favorite Items: $favoriteItemsCount"
    }
}
