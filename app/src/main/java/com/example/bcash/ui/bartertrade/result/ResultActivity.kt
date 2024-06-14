package com.example.bcash.ui.bartertrade.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.bcash.databinding.ActivityResultBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.ui.bartertrade.endportal.EndPortalActivity
import com.example.bcash.ui.main.MainActivity
import com.example.bcash.utils.ImageSettings.compressFileImage
import com.example.bcash.utils.ImageSettings.convertUriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var factory: ViewModelFactory
    private var file: File? = null
    private val resultViewModel: ResultViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the factory
        factory = ViewModelFactory.getInstance(this)

        // Setup views and listeners
        setupView()
        setupListener()
    }

    private fun setupView() {
        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri: Uri? = imageUriString?.let { Uri.parse(it) }
        val category = intent.getStringExtra("category")
        val condition = intent.getStringExtra("condition")

        val categories = listOf("Elektronik", "Pakaian", "Buku", "Peralatan Rumah Tangga")
        val conditions = listOf("Baru", "Bekas", "Rusak")

        if (imageUri != null) {
            file = convertUriToFile(imageUri, this)
            Log.e("TAG", "Converted file path: $file")

            Glide.with(this)
                .load(file)
                .into(binding.resultImage)
        } else {
            Log.e("TAG", "setupView: Image URI is null")
        }

        binding.dropdownCategory.setText(category)
        binding.dropdownCondition.setText(condition)

        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        binding.dropdownCategory.setAdapter(categoryAdapter)

        val conditionAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, conditions)
        binding.dropdownCondition.setAdapter(conditionAdapter)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListener() {
        binding.btnPost.setOnClickListener {
            postProduct()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun postProduct() {
        resultViewModel.getSession().observe(this@ResultActivity) { session ->
            if (file != null) {
                val compressedFile = compressFileImage(file as File)
                val requestImageFile = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", compressedFile.name, requestImageFile)

                val name = binding.edtName.text.toString()
                val price = binding.edtPrice.text.toString()
                val condition = binding.dropdownCondition.text.toString()
                val category = binding.dropdownCategory.text.toString()
                val description = binding.edtDescription.text.toString().takeIf { it.isNotBlank() }?.toRequestBody("text/plain".toMediaTypeOrNull()) ?: "".toRequestBody("text/plain".toMediaTypeOrNull())

                Log.e("TAG", "Image file: $file")
                Log.e("TAG", "Image Multipart: $imageMultipart")
                Log.e("TAG", "Request Image File: $requestImageFile")
                Log.e("TAG", "Name: $name, Price: $price, Description: ${description.contentType()}, Condition: $condition, Category: $category")

                resultViewModel.uploadProduct(session.token,name,description,condition,category,price,imageMultipart,session.name,session.userId)

                resultViewModel.uploadProductResponse.observe(this@ResultActivity) { response ->
                    if (response.error != true && !binding.edtName.text.isNullOrEmpty()) {
                        showToast("Product uploaded successfully")
                        val intent = Intent(this@ResultActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("TAG", "postProduct: ${response.message}")
                        showToast("Failed to upload product")
                    }
                }
            } else {
                showToast("File is null")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
