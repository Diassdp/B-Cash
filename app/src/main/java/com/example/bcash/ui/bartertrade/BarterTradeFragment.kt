package com.example.bcash.ui.bartertrade

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bcash.databinding.FragmentBarterTradeBinding
import com.example.bcash.databinding.FragmentDashboardBinding
import com.example.bcash.databinding.FragmentShopBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.ui.dashboard.DashboardAdapter
import com.example.bcash.ui.dashboard.DashboardViewModel

class BarterTradeFragment : Fragment() {

    private var _binding: FragmentBarterTradeBinding? = null
    private val binding get() = _binding!!

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }

    private val barterTradeViewModel: BarterTradeViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root
        // List of items for the dropdowns
        val categories = listOf("Elektronik", "Pakaian", "Buku", "Peralatan Rumah Tangga")
        val conditions = listOf("Baru", "Bekas", "Rusak")

        // Adapter for categories
        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, categories)
        binding.dropdownCategory.setAdapter(categoryAdapter)

        // Adapter for conditions
        val conditionAdapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, conditions)
        binding.dropdownCondition.setAdapter(conditionAdapter)

        return view
    }
}
