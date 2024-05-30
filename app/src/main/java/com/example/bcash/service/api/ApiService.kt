package com.example.bcash.service.api

import com.example.bcash.service.response.LoginResponse
import com.example.bcash.service.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field(value = "email") email: String,
        @Field(value = "password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field(value = "name") name: String,
        @Field(value = "email") email: String,
        @Field(value = "password") password: String
    ): Call<RegisterResponse>

}