package com.example.bcash.ui.profile

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

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var token: String
    private lateinit var userId: String

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }
    private val viewModel: ProfileViewModel by viewModels { factory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.clInventory.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_inventoryFragment)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupView()
        setupListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        setupViewModel()
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

    private fun setupListener() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }
}
