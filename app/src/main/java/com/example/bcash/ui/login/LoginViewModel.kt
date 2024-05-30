package com.example.bcash.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bcash.model.SessionModel
import com.example.bcash.service.repository.Repository
import com.example.bcash.service.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    val loginResponse: LiveData<LoginResponse> = repository.loginResponse
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun postLogin(email: String, password: String) {
        viewModelScope.launch {
            repository.postLogin(email, password)
            Log.d(TAG, "postLogin: Success")
        }
    }

    fun saveSession(session: SessionModel) {
        viewModelScope.launch {
            repository.saveSession(session)
            Log.d(TAG, "saveSession: Success")
        }
    }

    fun login() {
        viewModelScope.launch {
            repository.login()
            Log.d(TAG, "login: Success")
        }
    }

    companion object {
        const val TAG = "LoginActivityViewModel"
    }
}