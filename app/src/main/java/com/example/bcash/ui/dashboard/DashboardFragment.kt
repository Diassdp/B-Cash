package com.example.bcash.ui.dashboard

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcash.R
import com.example.bcash.databinding.FragmentDashboardBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.ui.bartertrade.feature.BarterTradeActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DashboardAdapter
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }

    private val viewModel: DashboardViewModel by viewModels { factory }

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        setupView()
        setupListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        setupSearch()
        insertData()
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
            binding.searchView.requestFocus()
        }
    }

    private fun setupView() {
        setupViewFlipper()
        setupAdapter()
        setupCategoryButtons()
    }

    private fun setupAdapter() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = DashboardAdapter()
        recyclerView.adapter = adapter
    }

    private fun insertData() {
        viewModel.getAllProduct.observe(viewLifecycleOwner) { pagingData ->
            Log.d("DashboardFragment", "PagingData received: ${pagingData}")
            adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
    }


    private fun setupListener() {
        binding.btnTradeBarter.setOnClickListener {
            val intent = Intent(requireContext(), BarterTradeActivity::class.java)
            startActivity(intent)
        }
        setupSwipeRefresh()
    }

    private fun setupViewFlipper() {
        val viewFlipper = binding.viewFlipper
        viewFlipper.setInAnimation(context, R.anim.slide_in_left)
        viewFlipper.setOutAnimation(context, R.anim.slide_in_right)
        viewFlipper.flipInterval = 8000
        viewFlipper.startFlipping()
    }

    private fun setupCategoryButtons() {
        var category = binding.btnBarang1.toString()
        val action = "category"
        binding.btnBarang1.setOnClickListener {
            category = binding.tvBarang1.text.toString()
            navigateToShopFragment(category, action)
        }
        binding.btnBarang2.setOnClickListener {
            category = binding.tvBarang2.text.toString()
            navigateToShopFragment(category, action)
        }
        binding.btnBarang3.setOnClickListener {
            category = binding.tvBarang3.text.toString()
            navigateToShopFragment(category, action)
        }
        binding.btnBarang4.setOnClickListener {
            category = binding.tvBarang4.text.toString()
            navigateToShopFragment(category, action)
        }
        binding.btnBarang5.setOnClickListener {
            category = binding.tvBarang5.text.toString()
            navigateToShopFragment(category, action)
        }
        binding.btnBarang6.setOnClickListener {
            category = binding.tvBarang6.toString()
            navigateToShopFragment(category, action)
        }
        binding.btnBarang7.setOnClickListener {
            category = binding.tvBarang7.toString()
            navigateToShopFragment(category, action)
        }
        binding.btnBarang8.setOnClickListener {
            category = binding.tvBarang8.toString()
            navigateToShopFragment(category, action)
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            refreshFragment()
        }
    }

    private fun refreshFragment() {
        insertData()
        swipeRefreshLayout.isRefreshing = false
    }


    private fun navigateToShopFragment(data: String, action: String) {
        val navigate = DashboardFragmentDirections.actionDashboardToShop(data, action)
        findNavController().navigate(navigate)
    }

    private fun setupSearch() {
        searchView = binding.searchView
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                navigateToShopFragment(query, "search")
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }
}
