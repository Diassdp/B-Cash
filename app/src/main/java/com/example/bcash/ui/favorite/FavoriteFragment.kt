package com.example.bcash.ui.favorite

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.convertTo
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
        setupView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        setupViewModel()
        countFavoriteItems()
    }

    private fun setupView(){
        setupAdapter()
    }

    private fun setupViewModel() {
        viewModel.getSession().observe(viewLifecycleOwner) { session ->
            session?.let {
                Log.e("FavoriteFragment","Starting Fetching")
                viewModel.getWishlist(it.token, it.userId)
            }
        }

        viewModel.wishlistResponse.observe(viewLifecycleOwner) { response ->
            if (response.error == false) {
                if (response.wishlist != null) {
                    Log.e("FavoriteFragment","Fetching Success")
                    adapter.setData(response.wishlist)
                } else {
                    Log.e("FavoriteFragment","Wishlist Empty")
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("FavoriteFragment","Fetching Failed Error : ${response.message}")
                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupAdapter() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = FavoriteAdapter()
        recyclerView.adapter = adapter
    }

    private fun countFavoriteItems() {
        val WishlistItemsCount = adapter.itemCount
        binding.tvFavCount.text = "Wishlist Items: $WishlistItemsCount"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
