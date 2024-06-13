package com.example.bcash.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bcash.model.SessionModel
import com.example.bcash.service.repository.Repository
import com.example.bcash.service.response.GetWishlistResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {
    private val _wishlistResponse: LiveData<GetWishlistResponse> = repository.getWishlistResponse
    val wishlistResponse: LiveData<GetWishlistResponse>
        get() = _wishlistResponse

    fun postWishlist(token: String, userId: String, productId: String) {
        viewModelScope.launch {
            repository.postWishlist(token, userId, productId)
        }
    }
    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }

    fun deleteWishlist(token: String, userId: String, productId: String) {
        viewModelScope.launch {
            repository.deleteWishlist(token, userId, productId)
        }
    }

    fun deleteInventory(token: String, userId: String, productId: String) {
        viewModelScope.launch {
            repository.deleteInventory(token, userId, productId)
        }
    }
}