package com.example.bcash.ui.bartertrade.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.bcash.R
import com.example.bcash.databinding.ActivityResultBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.ui.main.MainActivity
import com.example.bcash.utils.ImageSettings.compressFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var photoPath: String
    private var file: File? = null
    private val resultViewModel: ResultViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setupView()
        setupListener()

        // Set OnApplyWindowInsetsListener on the root view
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun setupView(){
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri: Uri? = imageUriString?.let { Uri.parse(it) }
        val category = intent.getStringExtra("category")
        val condition = intent.getStringExtra("condition")

        if (imageUri != null) {
            Glide.with(this)
                .load(imageUri)
                .into(binding.resultImage)
        }

        binding.tvResultCategory.text = category ?: "(No Category)"
        binding.tvResultCondition.text = condition ?: "(No Condition)"
    }

    private fun setupListener(){
        binding.btnPost.setOnClickListener {
            postProduct()
        }
    }

    private fun postProduct(){
        resultViewModel.getSession().observe(this@ResultActivity) {
            if (file != null) {
                val compressedFile = compressFileImage(file as File)
                val requestImageFile = compressedFile.asRequestBody("image/".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    compressedFile.name,
                    requestImageFile
                )
                val name = binding.edtName.text.toString()
                val price = binding.tvResultPrice.text.toString()
                val condition = binding.tvResultCondition.text.toString()
                val category = binding.tvResultCondition.text.toString()
                val description = binding.edtDescription.text.toString().takeIf { it.isNotBlank() }?.toRequestBody("text/plain".toMediaType()) ?: "".toRequestBody("text/plain".toMediaType())

                resultViewModel.uploadProduct(it.token, name, price,description,condition, category, imageMultipart)

                resultViewModel.uploadProductResponse.observe(this@ResultActivity) { response ->
                    if (response.error != true && !binding.edtName.text.isNullOrEmpty()) {
                        showToast("Story uploaded successfully")
                        val intent = Intent(this@ResultActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        showToast("Failed to upload story")
                    }
                }
            }
        }

    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
