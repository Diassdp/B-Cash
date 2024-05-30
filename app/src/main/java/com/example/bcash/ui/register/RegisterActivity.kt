package com.example.bcash.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bcash.R
import com.example.bcash.databinding.ActivityLoginBinding
import com.example.bcash.databinding.ActivityRegisterBinding
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.ui.login.LoginActivity
import com.example.bcash.ui.login.LoginViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var factory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        // TODO: Uncomment this If you want to test Register
//        setupListeners()


        // TODO: Delete this after Register worked & Comment this if you want to test register
        findViewById<Button>(R.id.btn_register).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        //
    }

    private fun setupView(){
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || password.length < 8) {
                if (name.isEmpty()) binding.edtName.error = "Email cannot be empty"
                if (email.isEmpty()) binding.edtEmail.error = "Email cannot be empty"
                if (password.isEmpty()) binding.edtPassword.error = "Password cannot be empty"
                if (password.length < 8) binding.edtPassword.error = "Password must be at least 8 characters"
                toastMessage("Register Failed")
            } else {
                loadingToggle()
                registerAction(name, email, password)
            }
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun registerAction(name: String, email: String, password: String) {
        registerViewModel.postRegister(name, email, password)
        registerViewModel.registerResponse.observe(this@RegisterActivity) { response ->
            if (response.error != true) {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            } else {
                Log.e(TAG, "Registration error: ${response.message}")
                Toast.makeText(
                    this@RegisterActivity,
                    "Registration failed: ${response.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadingToggle() {
        registerViewModel.isLoading.observe(this@RegisterActivity) {
//            binding.ProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}