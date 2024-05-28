package com.example.bcash.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bcash.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Find the inventory ConstraintLayout and set an onClickListener
        view.findViewById<View>(R.id.cl_inventory).setOnClickListener {
            // Navigate to the InventoryFragment
            findNavController().navigate(R.id.action_profileFragment_to_inventoryFragment)
        }

        return view
    }
}
