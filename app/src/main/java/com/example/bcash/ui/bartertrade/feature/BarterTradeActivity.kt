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
import com.example.bcash.utils.getImageUri
import com.yalantis.ucrop.UCrop
import java.io.File

class BarterTradeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBarterTradeBinding
    private var currentImageUri: Uri? = null
    private var croppedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        setupListener()
        setupPermissions()
    }

    private fun setupView() {
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
    }

    private fun setupListener() {
        binding.btnGalery.setOnClickListener {
            startGallery()
        }
        binding.btnPhoto.setOnClickListener {
            startCamera()
        }
        binding.btnAnalyse.setOnClickListener {
            analyzeImage()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            val uri = currentImageUri
            if (uri != null) {
                startUCrop(uri)
                Glide.with(this)
                    .load(uri)
                    .into(binding.resultImage)
            } else {
                showToast("Failed to get image URI")
            }
        }
    }

    private fun startUCrop(sourceUri: Uri) {
        val fileName = "cropped_image_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, fileName))
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
            .start(this)
    }

    private fun analyzeImage() {
        val category = binding.dropdownCategory.text.toString()
        val condition = binding.dropdownCondition.text.toString()

        val uri = currentImageUri
        if (uri != null && category.isNotEmpty() && condition.isNotEmpty()) {
            moveToResult(category, condition, uri.toString())
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

    private fun startGallery() {
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
            if (selectedImg != null) {
                currentImageUri = selectedImg
                startUCrop(selectedImg)
                Glide.with(this)
                    .load(selectedImg)
                    .into(binding.resultImage)
            } else {
                showToast("Failed to get image URI")
            }
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
