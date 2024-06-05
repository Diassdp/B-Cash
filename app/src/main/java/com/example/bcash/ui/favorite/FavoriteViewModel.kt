package com.example.bcash.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bcash.service.repository.Repository
import com.example.bcash.service.response.ProductItem

class FavoriteViewModel(private val repository: Repository) : ViewModel() {
    val getWishlistedProduct: LiveData<PagingData<ProductItem>> = repository.getProduct().cachedIn(viewModelScope)
}