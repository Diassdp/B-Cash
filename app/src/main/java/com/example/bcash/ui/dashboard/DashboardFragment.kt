package com.example.bcash.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcash.R
import com.example.bcash.databinding.FragmentDashboardBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.service.response.ProductItem
import com.example.bcash.ui.bartertrade.feature.BarterTradeActivity

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DashboardAdapter
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }

    private val dashboardViewModel: DashboardViewModel by viewModels { factory }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        setupView()
        setupListener()
        setupUser()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        dashboardViewModel.getAllProduct.observe(viewLifecycleOwner) { pagingData ->
            if (pagingData != null) {
//                adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                adapter.submitData(viewLifecycleOwner.lifecycle, PagingData.from(dummyData))
            }
        }

        setupAdapter()
    }


    private fun setupView() {
        setupViewFlipper()
        setupCategoryButtons()
    }

    private fun setupListener(){
        binding.btnTradeBarter.setOnClickListener {
            moveToTradeBarter()
        }
    }

    private fun moveToTradeBarter(){
        val intent = Intent(requireContext(), BarterTradeActivity::class.java)
        startActivity(intent)
    }

    private fun setupViewFlipper() {
        val viewFlipper = binding.viewFlipper
        viewFlipper.setInAnimation(context, R.anim.slide_in_left)
        viewFlipper.setOutAnimation(context, R.anim.slide_in_right)
        viewFlipper.flipInterval = 8000
        viewFlipper.startFlipping()
    }

    private fun setupCategoryButtons() {
        val btnBarang1 = binding.btnBarang1
        btnBarang1.setOnClickListener {
            // TODO: Implement Category Logic
        }
    }

    private fun setupAdapter() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = DashboardAdapter()
        recyclerView.adapter = adapter
    }

    private fun setupUser() {

    }

    val dummyData = listOf(
        ProductItem("1", "Product 1", "https://images-ext-1.discordapp.net/external/8YwJuF5LQA0QykOKHTHehSNHDbSnenl0QWtyVNszhe8/https/img.lazcdn.com/g/p/18de5a0582d6fbfbce7b3dffec049e6c.jpg_720x720q80.jpg_.webp?format=webp&width=676&height=676", "dummy_image_url","100K","baju","baru","Bob" ),
        ProductItem("2", "Product 2", "https://images-ext-1.discordapp.net/external/UtxCvcjDcuhkd4iWogrAkZdxPITsyGppglQdRBc7nUo/https/static4.depositphotos.com/1007248/289/i/450/depositphotos_2891812-stock-photo-plastic-dummy.jpg?format=webp", "dummy_image_url","200K","baju","bekas","Builder"),
        ProductItem("3", "Product 3", "https://images-ext-1.discordapp.net/external/UtxCvcjDcuhkd4iWogrAkZdxPITsyGppglQdRBc7nUo/https/static4.depositphotos.com/1007248/289/i/450/depositphotos_2891812-stock-photo-plastic-dummy.jpg?format=webp", "dummy_image_url","300K","elektronik","baru","opak"),
        ProductItem("4", "Product 4", "https://images-ext-1.discordapp.net/external/UtxCvcjDcuhkd4iWogrAkZdxPITsyGppglQdRBc7nUo/https/static4.depositphotos.com/1007248/289/i/450/depositphotos_2891812-stock-photo-plastic-dummy.jpg?format=webp", "dummy_image_url","400K","elektronik","baru","yonathan"),
    )

}
