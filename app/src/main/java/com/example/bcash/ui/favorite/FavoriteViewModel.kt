package com.example.bcash.ui.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bcash.model.SessionModel
import com.example.bcash.service.repository.Repository
import com.example.bcash.service.response.GetWishlistResponse
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: Repository) : ViewModel() {

    private val _wishlistResponse: LiveData<GetWishlistResponse> = repository.getWishlistResponse
    val wishlistResponse: LiveData<GetWishlistResponse> get() = _wishlistResponse

    fun getWishlist(token: String, userId: String) {
        viewModelScope.launch {
            try {
                repository.getWishlist(token, userId)
                Log.d("FavoriteViewModel", "Wishlist fetched successfully")
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Error fetching wishlist: ${e.message}", e)
            }
        }
    }

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }
}


