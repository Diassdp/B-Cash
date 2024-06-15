package com.example.bcash.service.api

import com.example.bcash.service.response.AddProductResponse
import com.example.bcash.service.response.AddToWishlistResponse
import com.example.bcash.service.response.AiProductResponse
import com.example.bcash.service.response.ConfirmTradeRequestResponse
import com.example.bcash.service.response.DeleteFromWishlistResponse
import com.example.bcash.service.response.DeleteInventoryResponse
import com.example.bcash.service.response.GetInventoryResponse
import com.example.bcash.service.response.GetProductResponse
import com.example.bcash.service.response.GetProfileResponse
import com.example.bcash.service.response.GetWishlistResponse
import com.example.bcash.service.response.LoginResponse
import com.example.bcash.service.response.PostTradeRequestResponse
import com.example.bcash.service.response.RegisterResponse
import com.example.bcash.service.response.GetTradeRequestResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
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
        @Header("authorization") token: String,
        @Part(value = "product") product: String,
        @Part(value = "description") description: RequestBody,
        @Part(value = "condition") condition: String,
        @Part(value = "category") category: String,
        @Part(value = "price") price: String,
        @Part photo: MultipartBody.Part,
        @Part(value = "username") username: String,
        @Part(value = "userId") userId: String,
        ): Call<AddProductResponse>

    @Multipart
    @POST("product")
    fun sendImageAI(
        @Header("authorization") token: String,
        @Part photo: MultipartBody.Part,
        ): Call<AiProductResponse>

    @GET("product")
    suspend fun getAllProduct(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<GetProductResponse>

    @GET("product")
    suspend fun getProductByCategory(
        @Query("category") category: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<GetProductResponse>

    @GET("product")
    suspend fun getProductBySearch(
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<GetProductResponse>

    @GET("profile")
    suspend fun getProfile(
        @Header("authorization") token: String,
        @Query("userId") userId: String
    ): Response<GetProfileResponse>

    @GET("wishlist")
    suspend fun getWishlist(
        @Header("authorization") token: String,
        @Query("userId") userId: String
    ): Response<GetWishlistResponse>

    @FormUrlEncoded
    @POST("wishlist")
    fun addToWishlist(
        @Header("authorization") token: String,
        @Field("userId") userId: String,
        @Field("productId") productId: String
    ): Call<AddToWishlistResponse>

    @DELETE("wishlist")
    fun deleteFromWishlist(
        @Header("authorization") token: String,
        @Query("userId") userId: String,
        @Query("productId") productId: String
    ): Call<DeleteFromWishlistResponse>

    @GET("inventory")
    suspend fun getUserInventory(
        @Header("authorization") token: String,
        @Query("userId") userId: String
    ): Response<GetInventoryResponse>

    @DELETE("inventory")
    fun deleteUserInventory(
        @Header("authorization") token: String,
        @Query("userId") userId: String,
        @Query("productId") productId: String
    ): Call<DeleteInventoryResponse>

    @FormUrlEncoded
    @POST("trade-request")
    fun createTradeRequest(
        @Header("authorization") token: String,
        @Field("itemId1") itemIdSeller: String,
        @Field("itemId2") itemIdBuyer: String,
        @Field("username1") userIdSeller: String,
        @Field("username2") userIdBuyer: String,
    ): Call<PostTradeRequestResponse>

    @FormUrlEncoded
    @PATCH("trade-request/{trade_id}/confirm")
    fun confirmTradeRequest(
        @Header("authorization") token: String,
        @Field("trade_id") tradeId: Int,
        @Field("userId") userId: String,
        @Field("confirmed") confirmed: Boolean
    ): Call<ConfirmTradeRequestResponse>

    @GET("trade-request/{trade_id}")
    fun getTradeRequest(
        @Header("Authorization") token: String,
        @Field("trade_id") tradeId: Int
    ): Call<GetTradeRequestResponse>
}