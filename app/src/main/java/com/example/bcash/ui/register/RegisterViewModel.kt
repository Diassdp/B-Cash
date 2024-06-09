package com.example.bcash.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bcash.service.repository.Repository
import com.example.bcash.service.response.RegisterResponse

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    val registerResponse: LiveData<RegisterResponse> = repository.registerResponse
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun postRegister(name: String, email: String, password: String, address: String, phone: String) {
        repository.postRegister(name, email, password, address, phone)
    }
}