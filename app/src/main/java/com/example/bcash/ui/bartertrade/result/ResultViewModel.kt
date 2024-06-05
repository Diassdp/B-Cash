package com.example.bcash.ui.bartertrade.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bcash.model.SessionModel
import com.example.bcash.service.repository.Repository
import com.example.bcash.service.response.AddProductResponse
import com.example.bcash.ui.login.LoginViewModel.Companion.TAG
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ResultViewModel(private val repository: Repository) : ViewModel()  {
    val uploadProductResponse: LiveData<AddProductResponse> = repository.addProductsResponse
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun uploadProduct(token: String, name: String, price: String, description: RequestBody, condition: String, category: String,photo: MultipartBody.Part) {
        repository.postAddProducts(token,name,price,description,condition, category ,photo)
        Log.d(TAG, "addStory: $uploadProductResponse")
    }

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }

}