package com.example.bcash.ui.bartertrade.feature

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bcash.databinding.FragmentBarterTradeBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.ui.login.LoginActivity
import com.example.bcash.ui.main.MainViewModel

class BarterTradeFragment : Fragment() {

    private lateinit var binding: FragmentBarterTradeBinding

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }
    private val viewModel: MainViewModel by viewModels { factory }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBarterTradeBinding.inflate(inflater, container, false)
        val view = binding.root
        setupListener()
        playAnimation()
        return view
    }

    private fun setupListener(){
        viewModel.getSession().observe(viewLifecycleOwner) {session ->
            binding.btnStart.setOnClickListener {
                if (session.statusLogin) {
                    moveToBarterTrade()
                } else {
                    binding.clLogin.visibility = View.VISIBLE
                }
            }
            binding.btnStarted.setOnClickListener {
                if (session.statusLogin) {
                    moveToBarterTrade()
                } else {
                    binding.clLogin.visibility = View.VISIBLE
                }
            }
        }
        binding.clLogin.setOnClickListener {
            binding.clLogin.visibility = View.GONE
        }

        binding.btnLogin.setOnClickListener {
            moveToLogin()
        }
    }

    private fun moveToLogin() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity()
    }


    private fun moveToBarterTrade(){
        val intent = Intent(requireContext(), BarterTradeActivity::class.java)
        startActivity(intent)
    }

    private fun playAnimation() {
        val imageViewAnimation = ObjectAnimator.ofFloat(binding.imgLock, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        imageViewAnimation.start()

    }
}