package com.example.bcash.ui.bartertrade.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bcash.model.SessionModel
import com.example.bcash.service.repository.Repository
import com.example.bcash.service.response.TradeRequestResponse
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: Repository) : ViewModel() {
    private val _tradeRequestResponse = MutableLiveData<TradeRequestResponse>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    val tradeRequestReponse: LiveData<TradeRequestResponse> get() = _tradeRequestResponse

    init {
        repository.tradeRequestResponse.observeForever {
            _tradeRequestResponse.postValue(it)
        }
    }

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }

    fun createTradeRequest(token: String, itemIdSeller: String, itemIdBuyer: String, usernameSeller: String, usernameBuyer: String) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.createTradeRequest(token, itemIdSeller, itemIdBuyer, usernameSeller, usernameBuyer)
            _isLoading.value = false
        }
    }
}