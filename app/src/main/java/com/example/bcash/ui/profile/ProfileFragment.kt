package com.example.bcash.ui.profile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bcash.R
import com.example.bcash.databinding.FragmentProfileBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.ui.login.LoginActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }
    private val viewModel: ProfileViewModel by viewModels { factory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupUser() {
        viewModel.getSession().observe(viewLifecycleOwner) { session ->
            if (session.statusLogin) {
                binding.clLogin.visibility=View.GONE
                setupViewModel()
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        setupUser()
        playAnimation()
    }

    private fun setupViewModel() {
        viewModel.getSession().observe(viewLifecycleOwner) { session ->
            session?.let {
                viewModel.getProfile(it.token, it.userId)
            }
        }

        viewModel.profile.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.tvName.text = user.profile.name
                binding.tvMyname.text = user.profile.name
                binding.tvEmail.text = user.profile.email
                binding.tvMyemail.text = user.profile.name
                binding.tvPhone.text = user.profile.phone
                binding.tvAddress.text = user.profile.address
            }
        }
    }
    private fun moveToLogin() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun setupListener() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            moveToLogin()
        }

        binding.clInventory.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_inventoryFragment)
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
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
