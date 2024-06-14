package com.example.bcash.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bcash.model.SessionModel
import com.example.bcash.service.repository.Repository
import com.example.bcash.service.response.GetProfileResponse
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    private val _profile : LiveData<GetProfileResponse> = repository.getProfileResponse
    val profile: LiveData<GetProfileResponse> = _profile

    fun getProfile(token: String, userId: String) {
        viewModelScope.launch {
            repository.getProfile(token,userId)
        }
    }

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}
