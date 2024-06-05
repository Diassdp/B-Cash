package com.example.bcash.ui.bartertrade.feature

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bcash.databinding.ActivityBarterTradeBinding
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.bcash.ui.bartertrade.result.ResultActivity

class BarterTradeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBarterTradeBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBarterTradeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // List of items for the dropdowns
        val categories = listOf("Elektronik", "Pakaian", "Buku", "Peralatan Rumah Tangga")
        val conditions = listOf("Baru", "Bekas", "Rusak")

        // Adapter for categories
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        binding.dropdownCategory.setAdapter(categoryAdapter)

        // Adapter for conditions
        val conditionAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, conditions)
        binding.dropdownCondition.setAdapter(conditionAdapter)

        // Set OnApplyWindowInsetsListener on the root view
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupView()
        setupListener()
        setupPermissions()
    }

    private fun setupView(){

    }

    private fun setupListener(){
        binding.btnGalery.setOnClickListener {
            startGallery()
        }
        binding.btnPhoto.setOnClickListener {
            // Implement photo capture logic
        }
        binding.btnAnalyse.setOnClickListener {
            analyzeImage()
        }
    }

    private fun analyzeImage() {
        val category = binding.dropdownCategory.text.toString()
        val condition = binding.dropdownCondition.text.toString()

        if (currentImageUri != null && category.isNotEmpty() && condition.isNotEmpty()) {
            moveToResult(category, condition, currentImageUri.toString())
        } else {
            showToast("Please select an image, category, and condition")
        }
    }

    private fun moveToResult(category: String, condition: String, imageUri: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("category", category)
        intent.putExtra("condition", condition)
        intent.putExtra("imageUri", imageUri)
        startActivity(intent)
    }

    private fun startGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data
            selectedImg?.let { uri ->
                currentImageUri = uri
                Glide.with(this)
                    .load(currentImageUri)
                    .into(binding.resultImage)
            } ?: showToast("Failed to get image URI")
        }
    }


    private fun setupPermissions() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
