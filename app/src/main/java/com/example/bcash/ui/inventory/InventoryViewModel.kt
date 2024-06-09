package com.example.bcash.ui.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bcash.model.SessionModel
import com.example.bcash.service.repository.Repository
import com.example.bcash.service.response.GetInventoryResponse
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: Repository) : ViewModel() {
    private val _inventoryResponse: LiveData<GetInventoryResponse> = repository.getInventoryResponse
    val inventoryResponse: LiveData<GetInventoryResponse> = _inventoryResponse

    fun getInventory(token: String, userId: String) {
        viewModelScope.launch {
            repository.getInventory(token, userId)
        }
    }

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }
}
