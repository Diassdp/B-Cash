package com.example.bcash.ui.login

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
import com.example.bcash.MainActivity
import com.example.bcash.R
import com.example.bcash.databinding.ActivityLoginBinding
import com.example.bcash.model.SessionModel
import com.example.bcash.model.ViewModelFactory
import com.example.bcash.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        // TODO: Uncomment this If you want to test Register
//        setupListeners()
        //


        // TODO: Delete this after Register worked & Comment this if you want to test register
        findViewById<Button>(R.id.btn_login).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        //
    }

    private fun setupView() {
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListener() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) binding.edtEmail.error = "Email cannot be empty"
                if (password.isEmpty()) binding.edtPassword.error = "Password cannot be empty"
                toastMessage("Login Failed")
            } else {
                loadingToggle()
                loginAction()
                loginViewModel.login()
                moveToMain()
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun moveToMain() {
        loginViewModel.loginResponse.observe(this@LoginActivity) { response ->
            if (response.error != true) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun loadingToggle() {
        loginViewModel.isLoading.observe(this) {
//            binding.ProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun loginAction() {
        binding.apply {
            loginViewModel.postLogin(
                edtEmail.text.toString(),
                edtPassword.text.toString()
            )
        }

        loginViewModel.loginResponse.observe(this) { response ->
            if (response.error != true) {
                toastMessage("Login Success")
                saveSession(
                    SessionModel(
                        TOKEN_KEY + (response.loginResult?.token.toString()),
                        response.loginResult?.name.toString(),
                        true
                    )
                )
            } else {
                Log.e(TAG, "Registration error: ${response.message}")
                Log.e(TAG, "Token: ${TOKEN_KEY + (response.loginResult?.token.toString())}")
                Toast.makeText(
                    this@LoginActivity,
                    "Registration failed: ${response.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveSession(session: SessionModel) {
        loginViewModel.saveSession(session)
    }

    companion object {
        private const val TAG = "LoginActivity"
        private const val TOKEN_KEY = "Bearer "
    }
}