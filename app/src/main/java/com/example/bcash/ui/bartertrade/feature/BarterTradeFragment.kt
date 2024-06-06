package com.example.bcash.ui.bartertrade.feature

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bcash.databinding.FragmentBarterTradeBinding

class BarterTradeFragment : Fragment() {

    private lateinit var binding: FragmentBarterTradeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBarterTradeBinding.inflate(inflater, container, false)
        val view = binding.root
        setupListener()
        return view
    }

    private fun setupListener(){
        binding.btnStarted.setOnClickListener {
            moveToBarterTrade()
        }
        binding.btnStarted.setOnClickListener {
            moveToBarterTrade()
        }
    }

    private fun moveToBarterTrade(){
        val intent = Intent(requireContext(), BarterTradeActivity::class.java)
        startActivity(intent)
    }
}