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

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }

    private val profileViewModel: ProfileViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.findViewById<View>(R.id.cl_inventory).setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_inventoryFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        setupView()
        setupListener()
    }

    private fun setupView() {
        profileViewModel.profile.observe(viewLifecycleOwner) { user ->
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
                profileViewModel.logout()
            }
        }
    }
