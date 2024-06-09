package com.example.bcash.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bcash.service.repository.Repository
import com.example.bcash.service.response.data.ProductItem

class ShopViewModel(private val repository: Repository) : ViewModel() {
    fun getProductsByCategory(category: String): LiveData<PagingData<ProductItem>> {
        return repository.getProductbyCategory(category).cachedIn(viewModelScope)
    }
}