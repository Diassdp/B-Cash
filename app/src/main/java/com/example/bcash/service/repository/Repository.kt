package com.example.bcash.service.repository

import android.annotation.SuppressLint
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
import com.example.bcash.service.paging.CategoryPagingSource
import com.example.bcash.service.paging.PagingSource
import com.example.bcash.service.paging.SearchPagingSource
import com.example.bcash.service.response.AddProductResponse
import com.example.bcash.service.response.AddToWishlistResponse
import com.example.bcash.service.response.ConfirmTradeRequestResponse
import com.example.bcash.service.response.DeleteFromWishlistResponse
import com.example.bcash.service.response.DeleteInventoryResponse
import com.example.bcash.service.response.GetInventoryResponse
import com.example.bcash.service.response.GetProductResponse
import com.example.bcash.service.response.GetProfileResponse
import com.example.bcash.service.response.GetTradeRequestResponse
import com.example.bcash.service.response.GetWishlistResponse
import com.example.bcash.service.response.LoginResponse
import com.example.bcash.service.response.PostTradeRequestResponse
import com.example.bcash.service.response.data.ProductItem
import com.example.bcash.service.response.data.Profile
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

    private val _postTradeRequestResponse = MutableLiveData<PostTradeRequestResponse>()
    val postTradeRequestResponse: LiveData<PostTradeRequestResponse> = _postTradeRequestResponse

    private val _confirmTradeRequestResponse = MutableLiveData<ConfirmTradeRequestResponse>()
    val confirmTradeRequestResponse: LiveData<ConfirmTradeRequestResponse> = _confirmTradeRequestResponse

    private val _getTradeRequestResponse = MutableLiveData<GetTradeRequestResponse>()
    val getTradeRequestResponse: LiveData<GetTradeRequestResponse> = _getTradeRequestResponse

    private val _getInventoryResponse = MutableLiveData<GetInventoryResponse>()
    val getInventoryResponse: LiveData<GetInventoryResponse> = _getInventoryResponse

    private val _deleteInventoryResponse = MutableLiveData<DeleteInventoryResponse>()
    val deleteInventoryResponse: LiveData<DeleteInventoryResponse> = _deleteInventoryResponse

    private val _getWishlistResponse = MutableLiveData<GetWishlistResponse>()
    val getWishlistResponse: LiveData<GetWishlistResponse> = _getWishlistResponse

    private val _addWishlistResponse = MutableLiveData<AddToWishlistResponse>()
    val addWishlistResponse: LiveData<AddToWishlistResponse> = _addWishlistResponse

    private val _deleteWishlistResponse = MutableLiveData<DeleteFromWishlistResponse>()
    val deleteWishlistResponse: LiveData<DeleteFromWishlistResponse> = _deleteWishlistResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun createTradeRequest(token: String, itemIdSeller: String, itemIdBuyer: String, userIdSeller: String, userIdBuyer: String) {
        toggleLoading(true)
        val client = api.createTradeRequest(token, itemIdSeller, itemIdBuyer, userIdSeller, userIdBuyer)

        client.enqueue(object : Callback<PostTradeRequestResponse> {
            override fun onResponse(call: Call<PostTradeRequestResponse>, response: Response<PostTradeRequestResponse>) {
                toggleLoading(false)
                if (response.isSuccessful) {
                    _postTradeRequestResponse.value = response.body()
                    showToast("Trade Request Created")
                } else {
                    val message = extractErrorMessage(response)
                    _postTradeRequestResponse.value = PostTradeRequestResponse(error = true, message = message)
                    showToast(message)
                    Log.e("Repository", "createTradeRequest onResponse: ${response.message()}, ${response.code()} $message")
                }
            }

            override fun onFailure(call: Call<PostTradeRequestResponse>, t: Throwable) {
                toggleLoading(false)
                val message = if (t is UnknownHostException) {
                    "No Internet Connection"
                } else {
                    t.message.toString()
                }
                _postTradeRequestResponse.value = PostTradeRequestResponse(error = true, message = message)
                showToast(message)
                Log.e("Repository", "createTradeRequest onFailure: $message")
            }
        })
    }

    fun confirmTradeRequest(token: String, tradeId: Int, userId: String, confirmed: Boolean) {
        toggleLoading(true)
        val client = api.confirmTradeRequest(token, tradeId, userId, confirmed)

        client.enqueue(object : Callback<ConfirmTradeRequestResponse> {
            override fun onResponse(call: Call<ConfirmTradeRequestResponse>, response: Response<ConfirmTradeRequestResponse>) {
                toggleLoading(false)
                if (response.isSuccessful) {
                    _confirmTradeRequestResponse.value = response.body()
                    showToast("Trade Request Confirmed")
                } else {
                    val message = extractErrorMessage(response)
                    _confirmTradeRequestResponse.value = ConfirmTradeRequestResponse(error = true, message = message)
                    showToast(message)
                    Log.e("Repository", "confirmTradeRequest onResponse: ${response.message()}, ${response.code()} $message")
                }
            }

            override fun onFailure(call: Call<ConfirmTradeRequestResponse>, t: Throwable) {
                toggleLoading(false)
                val message = if (t is UnknownHostException) {
                    "No Internet Connection"
                } else {
                    t.message.toString()
                }
                _confirmTradeRequestResponse.value = ConfirmTradeRequestResponse(error = true, message = message)
                showToast(message)
                Log.e("Repository", "confirmTradeRequest onFailure: $message")
            }
        })
    }

    fun getTradeRequest(token: String, tradeId: Int) {
        toggleLoading(true)
        val client = api.getTradeRequest(token, tradeId)
        client.enqueue(object : Callback<GetTradeRequestResponse> {
            override fun onResponse(call: Call<GetTradeRequestResponse>, response: Response<GetTradeRequestResponse>) {
                toggleLoading(false)
                if (response.isSuccessful) {
                    _getTradeRequestResponse.value = response.body()
                } else {
                    val message = extractErrorMessage(response)
                    _getTradeRequestResponse.value = GetTradeRequestResponse(error = true, message = message)
                    showToast(message)
                    Log.e("Repository", "getTradeRequest onResponse: ${response.message()}, ${response.code()} $message")
                }
            }

            override fun onFailure(call: Call<GetTradeRequestResponse>, t: Throwable) {
                toggleLoading(false)
                val message = if (t is UnknownHostException) {
                    "No Internet Connection"
                } else {
                    t.message.toString()
                }
                _getTradeRequestResponse.value = GetTradeRequestResponse(error = true, message = message)
                showToast(message)
                Log.e("Repository", "getTradeRequest onFailure: $message")
            }
        })
    }

    fun postRegister(name: String, email: String, password: String, address: String, phone: String) {
        toggleLoading(true)
        val client = api.register(name, email, phone, address, password)

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                toggleLoading(false)
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()
                } else {
                    val message = extractErrorMessage(response)
                    _registerResponse.value = RegisterResponse(true, message)
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
                } else {
                    val message = extractErrorMessage(response)
                    _loginResponse.value = LoginResponse(true, message)
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

    fun postAddProducts(token: String,productName : String,description : RequestBody, condition: String, category: String, price: String, image: MultipartBody.Part, username: String, userId: String) {
        toggleLoading(true)
        val client = api.addProduct(token,productName,description,condition,category,price,image,username,userId)

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

    suspend fun getProfile(token: String, userId: String): GetProfileResponse {
        toggleLoading(true)
        return try {
            val response = api.getProfile(token, userId)
            toggleLoading(false)
            val responseBody = response.body()
            Log.d("Repository", "getProfile: $responseBody")
            val result = GetProfileResponse(profile = responseBody?.profile ?: Profile(id = "", name = "", email = "", phone = "", address = ""), error = false, message = "")
            _getProfileResponse.postValue(result)
            result
        } catch (e: Exception) {
            toggleLoading(false)
            val message = if (e is UnknownHostException) {
                "No Internet Connection"
            } else {
                e.message ?: "Unknown error occurred"
            }
            showToast(message)
            Log.e("Repository", "getWishlist onFailure: $message")
            val result = GetProfileResponse(Profile(id = "", name = "", email = "", phone = "", address = ""), error = true, message = message)
            _getProfileResponse.postValue(result)
            result
        }
    }

    suspend fun getWishlist(token: String, userId: String): GetWishlistResponse {
        toggleLoading(true)
        return try {
            val response = api.getWishlist(token, userId)
            toggleLoading(false)
            val responseBody = response.body()
            val result = GetWishlistResponse(wishlist = responseBody?.wishlist ?: emptyList(), error = false, message = "")
            _getWishlistResponse.postValue(result)
            result
        } catch (e: Exception) {
            toggleLoading(false)
            val message = if (e is UnknownHostException) {
                "No Internet Connection"
            } else {
                e.message ?: "Unknown error occurred"
            }
            showToast(message)
            Log.e("Repository", "getWishlist onFailure: $message")
            val result = GetWishlistResponse(wishlist = emptyList(), error = true, message = message)
            _getWishlistResponse.postValue(result)
            result
        }
    }

    fun postWishlist(token: String, userId: String, productId: String) {
        toggleLoading(true)
        val client = api.addToWishlist(token,userId,productId)

        client.enqueue(object : Callback<AddToWishlistResponse>{
            override fun onResponse(call: Call<AddToWishlistResponse>, response: Response<AddToWishlistResponse>) {
                toggleLoading(false)
                if (response.isSuccessful) {
                    _addWishlistResponse.value = response.body()
                    showToast("Product Added to Wishlist")
                } else {
                    val message = extractErrorMessage(response)
                    _addWishlistResponse.value = AddToWishlistResponse(error = true, message = message)
                    showToast(message)
                    Log.e("Repository", "PostWishlist onResponse: ${response.message()}, ${response.code()} $message")
                }
            }
            override fun onFailure(call: Call<AddToWishlistResponse>, t: Throwable) {
                toggleLoading(false)
                val message = if (t is UnknownHostException) {
                    "No Internet Connection"
                } else {
                    t.message.toString()
                }
                _addWishlistResponse.value = AddToWishlistResponse(error = true, message = message)
                showToast(message)
                Log.e("Repository", "PostWishlist onFailure: $message")
            }
        })
    }

    fun deleteWishlist(token: String, userId: String, productId: String) {
        toggleLoading(true)
        val client = api.deleteFromWishlist(token, userId, productId)
        client.enqueue(object : Callback<DeleteFromWishlistResponse> {
            override fun onResponse(call: Call<DeleteFromWishlistResponse>, response: Response<DeleteFromWishlistResponse>) {
                toggleLoading(false)
                if (response.isSuccessful) {
                    _deleteWishlistResponse.value = response.body()
                } else {
                    val message = extractErrorMessage(response)
                    _deleteWishlistResponse.value = DeleteFromWishlistResponse(error = true, message = message)
                }
            }

            override fun onFailure(call: Call<DeleteFromWishlistResponse>, t: Throwable) {
                toggleLoading(false)
                val message = t.message ?: "Unknown error occurred"
                _deleteWishlistResponse.value = DeleteFromWishlistResponse(error = true, message = message)
                showToast(message)
            }
        })
    }

    fun deleteInventory(token: String, userId: String, productId: String) {
        toggleLoading(true)
        val client = api.deleteUserInventory(token, userId, productId)

        client.enqueue(object : Callback<DeleteInventoryResponse> {
            override fun onResponse(call: Call<DeleteInventoryResponse>, response: Response<DeleteInventoryResponse>) {
                toggleLoading(false)
                if (response.isSuccessful) {
                    _deleteInventoryResponse.value = response.body()
                } else {
                    val message = extractErrorMessage(response)
                    _deleteInventoryResponse.value = DeleteInventoryResponse(error = true, message = message)
                }
            }

            override fun onFailure(call: Call<DeleteInventoryResponse>, t: Throwable) {
                toggleLoading(false)
                val message = t.message ?: "Unknown error occurred"
                _deleteInventoryResponse.value = DeleteInventoryResponse(error = true, message = message)
                showToast(message)
            }
        })
    }


    suspend fun getInventory(token: String, userId: String): GetInventoryResponse {
        toggleLoading(true)
        return try {
            val response = api.getUserInventory(token, userId)
            toggleLoading(false)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val result = GetInventoryResponse(inventory = responseBody?.inventory ?: emptyList(), error = false, message = "")
                _getInventoryResponse.postValue(result)
                result
            } else {
                val message = extractErrorMessage(response)
                val result = GetInventoryResponse(inventory = emptyList(), error = true, message = message)
                _getInventoryResponse.postValue(result)
                result
            }
        } catch (e: Exception) {
            toggleLoading(false)
            val message = if (e is UnknownHostException) {
                "No Internet Connection"
            } else {
                e.message ?: "Unknown error occurred"
            }
            showToast(message)
            val result = GetInventoryResponse(inventory = emptyList(), error = true, message = message)
            _getInventoryResponse.postValue(result)
            result
        }
    }


    fun getProduct(): LiveData<PagingData<ProductItem>> {
        return Pager(config = PagingConfig(pageSize = 5), pagingSourceFactory = {
            PagingSource(api)
        }).liveData
    }

    fun getProductbyCategory(category: String): LiveData<PagingData<ProductItem>> {
        return Pager(config = PagingConfig(pageSize = 5), pagingSourceFactory = {
            CategoryPagingSource(api,category)
        }).liveData
    }

    fun getProductbySearch(search: String): LiveData<PagingData<ProductItem>> {
        return Pager(config = PagingConfig(pageSize = 5), pagingSourceFactory = {
            SearchPagingSource(api,search)
        }).liveData
    }

    fun getSession(): LiveData<SessionModel> {
        return preferences.getSession().asLiveData()
    }

    suspend fun isLogout(): Boolean {
        return preferences.shouldLogout()
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

        @SuppressLint("StaticFieldLeak")
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
