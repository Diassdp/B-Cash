package com.example.bcash.ui.bartertrade.endportal

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bcash.R
import com.example.bcash.databinding.ActivityEndPortalBinding
import com.example.bcash.databinding.ActivityTransactionBinding
import com.example.bcash.ui.main.MainActivity

class EndPortalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEndPortalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        setupListeners()
        playAnimation()
    }

    private fun setupView(){
        binding = ActivityEndPortalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListeners(){
        binding.btnHome.setOnClickListener {
            finish()
        }
    }

    private fun playAnimation() {
        val imageViewAnimation = ObjectAnimator.ofFloat(binding.imgDone, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        imageViewAnimation.start()

    }
}