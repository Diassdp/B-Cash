package com.example.bcash.service.api

import com.example.bcash.service.response.AddProductResponse
import com.example.bcash.service.response.GetProductResponse
import com.example.bcash.service.response.GetProfileResponse
import com.example.bcash.service.response.LoginResponse
import com.example.bcash.service.response.RegisterResponse
import com.example.bcash.service.response.TradeRequestResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

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
        @Field(value = "phone") phone: String,
        @Field(value = "address") address: String,
        @Field(value = "password") password: String
    ): Call<RegisterResponse>

    @Multipart
    @POST("product")
    fun addProduct(
        @Header("Authorization") token: String,
        @Part(value = "product") product: String,
        @Part(value = "description") description: RequestBody,
        @Part(value = "condition") condition: String,
        @Part(value = "category") category: String,
        @Part(value = "price") price: String,
        @Part photo: MultipartBody.Part
    ): Call<AddProductResponse>

    @GET("product")
    suspend fun getAllProduct(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<GetProductResponse>

    @GET("product")
    suspend fun getProductByCategory(
        @Header("Authorization") token: String,
        @Query("category") category: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<GetProductResponse>

    @GET("product")
    suspend fun getProductBySearch(
        @Header("Authorization") token: String,
        @Query("search") search: String,
    ): Response<GetProductResponse>

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Query("name") name: String
    ): Response<GetProfileResponse>

    @GET("inventory")
    suspend fun getInventory(
        @Header("Authorization") token: String,
        @Query("name") id: Int
    ): Response<GetProductResponse>

    @GET("wishlist")
    suspend fun getWishlist(
        @Header("Authorization") token: String,
        @Query("name") id: Int
    ): Response<GetProductResponse>

    @FormUrlEncoded
    @POST("trade-request")
    fun createTradeRequest(
        @Header("Authorization") token: String,
        @Field("itemId1") itemId1: String,
        @Field("itemId2") itemId2: String,
        @Field("userId1") userId1: String,
        @Field("userId2") userId2: String
    ): Call<TradeRequestResponse>

    @FormUrlEncoded
    @PATCH("trade-request/{trade_id}/confirm")
    fun confirmTradeRequest(
        @Header("Authorization") token: String,
        @Field("trade_id") tradeId: Int,
        @Field("userId") userId: String,
        @Field("confirmed") confirmed: Boolean
    ): Call<TradeRequestResponse>

    @GET("trade-request/{trade_id}")
    fun getTradeRequest(
        @Header("Authorization") token: String,
        @Field("trade_id") tradeId: Int
    ): Call<TradeRequestResponse>
}