package com.example.bcash.ui.bartertrade.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bcash.model.SessionModel
import com.example.bcash.service.repository.Repository
import com.example.bcash.service.response.AddProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ResultViewModel(private val repository: Repository) : ViewModel()  {
    val uploadProductResponse: LiveData<AddProductResponse> = repository.addProductsResponse
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun uploadProduct(token: String,productName : String,description : RequestBody, condition: String, category: String, price: String, image: MultipartBody.Part, username: String, userId: String) {
        repository.postAddProducts(token,productName,description,condition,category,price,image,username,userId)
    }

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }

}