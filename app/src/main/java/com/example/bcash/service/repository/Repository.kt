package com.example.bcash.service.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.bcash.model.SessionModel
import com.example.bcash.service.api.ApiService
import com.example.bcash.service.response.LoginResponse
import com.example.bcash.service.response.RegisterResponse
import com.example.bcash.utils.session.SessionPreferences
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class Repository (private val context: Context,
                  private val preferences: SessionPreferences,
                  private val api: ApiService
) {
    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun postRegister(name: String, email: String, password: String) {
        // TODO: Optimize and Correct Log And Messages
        toggleLoading(true)
        val client = api.register(name, email, password)

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
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
        // TODO: Optimize and Correct Log And Messages
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
