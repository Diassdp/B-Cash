package com.example.bcash.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bcash.model.SessionModel
import com.example.bcash.service.repository.Repository
import com.example.bcash.service.response.GetProductResponse
import com.example.bcash.service.response.data.ProductItem
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: Repository) : ViewModel() {
    val listProduct : LiveData<GetProductResponse> = repository.list
    val isLoading: LiveData<Boolean> = repository.isLoading
    val getAllProduct: LiveData<PagingData<ProductItem>> = repository.getProduct().cachedIn(viewModelScope)

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }

    fun isLogin(): Boolean {
        var status = false
        viewModelScope.launch {
            status = repository.isLogout()
        }
        return status
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}