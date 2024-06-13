package com.example.bcash.ui.favorite

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcash.databinding.FragmentFavoriteBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.ui.login.LoginActivity
import com.example.bcash.ui.detail.DetailFavoriteActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoriteAdapter
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(requireContext()) }
    private val viewModel: FavoriteViewModel by viewModels { factory }
    private lateinit var recyclerView: RecyclerView

    private val detailLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Refresh the data
            setupViewModel()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        setupUser()
        setupView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    private fun setupView(){
        setupListener()
        setupAdapter()
        playAnimation()
    }
    private fun setupListener() {
        binding.btnLogin.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupUser() {
        viewModel.getSession().observe(viewLifecycleOwner) { session ->
            if (!session.statusLogin) {
                binding.clLogin.visibility = View.VISIBLE
            } else {
                setupViewModel()
            }
        }
    }

    private fun setupViewModel() {
        viewModel.getSession().observe(viewLifecycleOwner) { session ->
            session?.let {
                viewModel.getWishlist(it.token, it.userId)
            }
        }

        viewModel.wishlistResponse.observe(viewLifecycleOwner) { response ->
            if (response.error == false) {
                if (response.wishlist != null) {
                    Log.e("FavoriteFragment","Fetching Success")
                    adapter.setData(response.wishlist)
                    var wishlistitemscount = response.wishlist.size
                    binding.tvFavCount.text = "$wishlistitemscount Items"
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
        adapter = FavoriteAdapter { productItem ->
            val intent = Intent(requireContext(), DetailFavoriteActivity::class.java).apply {
                putExtra(DetailFavoriteActivity.EXTRA_DATA, productItem)
            }
            detailLauncher.launch(intent)
        }
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
