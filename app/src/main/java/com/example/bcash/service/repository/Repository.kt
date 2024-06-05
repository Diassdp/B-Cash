package com.example.bcash.service.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.bcash.model.SessionModel
import com.example.bcash.service.api.ApiService
import com.example.bcash.service.paging.PagingSource
import com.example.bcash.service.response.AddProductResponse
import com.example.bcash.service.response.GetProductResponse
import com.example.bcash.service.response.GetProfileResponse
import com.example.bcash.service.response.LoginResponse
import com.example.bcash.service.response.ProductItem
import com.example.bcash.service.response.Profile
import com.example.bcash.service.response.RegisterResponse
import com.example.bcash.utils.session.SessionPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class Repository(private val context: Context, private val preferences: SessionPreferences, private val api: ApiService) {
    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _addProductsResponse = MutableLiveData<AddProductResponse>()
    val addProductsResponse: LiveData<AddProductResponse> = _addProductsResponse

    private val _getProductsResponse = MutableLiveData<GetProductResponse>()
    val list: LiveData<GetProductResponse> = _getProductsResponse

    private val _getProfileResponse = MutableLiveData<GetProfileResponse>()
    val getProfileResponse: LiveData<GetProfileResponse> = _getProfileResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postRegister(name: String, email: String, password: String, address: String, phone: String) {
        toggleLoading(true)
        val client = api.register(name, email, phone, address, password)

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                toggleLoading(false)
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()
                    showToast("Registration Successful")
                } else {
                    val message = extractErrorMessage(response)
                    _registerResponse.value = RegisterResponse(true, message)
                    showToast(message)
                    Log.e("TAG", "postRegister onResponse: ${response.message()}, ${response.code()} $message")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                toggleLoading(false)
                val message = if (t is UnknownHostException) {
                    "No Internet Connection"
                } else {
                    t.message.toString()
                }
                _registerResponse.value = RegisterResponse(true, message)
                showToast(message)
                Log.e("TAG", "postRegister onFailure: $message")
            }
        })
    }

    fun postLogin(email: String, password: String) {
        toggleLoading(true)
        val client = api.login(email, password)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                toggleLoading(false)
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                    showToast("Login Successful")
                } else {
                    val message = extractErrorMessage(response)
                    _loginResponse.value = LoginResponse(true, message)
                    showToast(message)
                    Log.e("Repository", "postLogin onResponse: ${response.message()}, ${response.code()} $message")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                toggleLoading(false)
                val message = if (t is UnknownHostException) {
                    "No Internet Connection"
                } else {
                    t.message.toString()
                }
                _loginResponse.value = LoginResponse(true, message)
                showToast(message)
                Log.e("Repository", "postLogin onFailure: $message")
            }
        })
    }

    fun postAddProducts(token: String, name: String, price: String, description: RequestBody, condition: String, category: String, photo: MultipartBody.Part) {
        toggleLoading(true)
        val client = api.addProduct(token, name, description, condition, price,category, photo)

        client.enqueue(object : Callback<AddProductResponse> {
            override fun onResponse(call: Call<AddProductResponse>, response: Response<AddProductResponse>) {
                toggleLoading(false)
                if (response.isSuccessful) {
                    _addProductsResponse.value = response.body()
                    showToast("Product Added")
                } else {
                    val message = extractErrorMessage(response)
                    _addProductsResponse.value = AddProductResponse(true, message)
                    showToast(message)
                    Log.e("Repository", "postAddProducts onResponse: ${response.message()}, ${response.code()} $message")
                }
            }

            override fun onFailure(call: Call<AddProductResponse>, t: Throwable) {
                toggleLoading(false)
                val message = if (t is UnknownHostException) {
                    "No Internet Connection"
                } else {
                    t.message.toString()
                }
                _addProductsResponse.value = AddProductResponse(true, message)
                showToast(message)
                Log.e("Repository", "postAddStory onFailure: $message")
            }
        })
    }

    fun getProduct(): LiveData<PagingData<ProductItem>> {
        return Pager(config = PagingConfig(pageSize = 5), pagingSourceFactory = {
            PagingSource(preferences, api)
        }).liveData
    }

    fun getSession(): LiveData<SessionModel> {
        return preferences.getSession().asLiveData()
    }

    suspend fun saveSession(session: SessionModel) {
        preferences.saveSession(session)
    }

    suspend fun login() {
        preferences.login()
    }

    suspend fun logout() {
        preferences.logout()
    }

    suspend fun getProfile(token: String, name: String) {
        toggleLoading(true)
        withContext(Dispatchers.IO) {
            try {
                val response = api.getProfile(token, name)
                withContext(Dispatchers.Main) {
                    toggleLoading(false)
                    if (response.isSuccessful) {
                        _getProfileResponse.value = response.body()
                    } else {
                        val message = extractErrorMessage(response)
                        _getProfileResponse.value = GetProfileResponse(Profile(id = "", name = "", email = "", phone = "", address = "", photoUrl = ""), error = true, message = message)
                        Log.e("Repository", "getProfile onResponse: ${response.message()}, ${response.code()} $message")
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    toggleLoading(false)
                    val message = if (t is UnknownHostException) {
                        "No Internet Connection"
                    } else {
                        t.message.toString()
                    }
                    _getProfileResponse.value = GetProfileResponse(Profile(id = "", name = "", email = "", phone = "", address = "", photoUrl = ""), error = true, message = message)
                    Log.e("Repository", "getProfile onFailure: $message")
                }
            }
        }
    }

    private fun toggleLoading(state: Boolean) {
        _isLoading.value = state
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun extractErrorMessage(response: Response<*>): String {
        return try {
            val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
            jsonObject?.getString("message") ?: "Unknown error"
        } catch (e: JSONException) {
            "Error parsing response"
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            context: Context,
            preferences: SessionPreferences,
            api: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(context, preferences, api)
            }.also { instance = it }
    }
}
