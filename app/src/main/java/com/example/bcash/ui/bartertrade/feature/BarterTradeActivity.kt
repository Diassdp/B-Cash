package com.example.bcash.ui.bartertrade.feature

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.bcash.databinding.ActivityBarterTradeBinding
import com.example.bcash.ui.bartertrade.result.ResultActivity
import com.example.bcash.utils.getImageUri
import com.example.bcash.ml.Model1
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.min

class BarterTradeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBarterTradeBinding
    private var currentImageUri: Uri? = null
    private var croppedImageUri: Uri? = null
    private val imageSize = 150 // Update this to match your model's expected input size

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
        val conditions = listOf("Baru", "Bekas", "Rusak")

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
        croppedImageUri = getImageUri(this)
        launcherIntentCamera.launch(croppedImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            croppedImageUri?.let { uri ->
                currentImageUri = uri
                startUCrop(uri)
                Log.d("BarterTradeActivity", "Image URI: $uri")
                Log.d("BarterTradeActivity", "Cropped Image URI: $croppedImageUri")
                showImage()
            } ?: showToast("Failed to get image URI")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                showCroppedImage(resultUri)
            } ?: showToast("Failed to crop image")
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            showToast("Crop error: ${cropError?.message}")
        }
    }

    private fun showCroppedImage(uri: Uri) {
        binding.resultImage.setImageURI(uri)
        croppedImageUri = uri
    }

    private fun showImage() {
        croppedImageUri?.let { uri ->
            Log.e("BarterTradeActivity", "Displaying image: $uri")
            binding.resultImage.setImageURI(uri)
        } ?: Log.d("BarterTradeActivity", "No image to display")
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
        val uri = croppedImageUri
        if (uri != null && condition.isNotEmpty()) {
            classifyImage(uri)
        } else {
            showToast("Please select an image, category, and condition")
        }
    }

    private fun classifyImage(uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            val image = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false)

            val model: Model1 = Model1.newInstance(applicationContext)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            val intValues = IntArray(imageSize * imageSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            var pixel = 0
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++]
                    byteBuffer.putFloat(((`val` shr 16) and 0xFF) / 255.0f)
                    byteBuffer.putFloat(((`val` shr 8) and 0xFF) / 255.0f)
                    byteBuffer.putFloat((`val` and 0xFF) / 255.0f)
                }
            }

            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val confidences = outputFeature0.floatArray
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf("Dresses", "Jackets", "Jeans", "Shirts", "Skirts", "Sweaters", "Tops", "Tshirt")
            val resultText = classes.getOrNull(maxPos) ?: "Unknown"

            showToast("Classified as: $resultText")
            var condition = binding.dropdownCondition.text.toString()

            moveToResult(resultText, condition, uri.toString())

            model.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("BarterTradeActivity", "Image format not supported: ${e.message}")
            showToast("Selected image format is not supported.")
        }
    }

    private fun moveToResult(category: String, condition: String, imageUri: String) {
        Log.e("BarterTradeActivity", "Moving to ResultActivity with Category: $category, Condition: $condition, Image URI: $imageUri")
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

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data
            selectedImg?.let { uri ->
                currentImageUri = uri
                startUCrop(uri)
                showImage()
            } ?: Log.d("launcherIntentGallery", "Failed to get image URI")
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
