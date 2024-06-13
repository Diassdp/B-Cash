package com.example.bcash.service.response.data

import com.google.gson.annotations.SerializedName

data class TradeRequest(
    @field:SerializedName("tradeId")
    val id: Int,

    @field:SerializedName("itemId1")
    val itemId1: String,

    @field:SerializedName("itemId2")
    val itemId2: String,

    @field:SerializedName("userId1")
    val userId1: String,

    @field:SerializedName("userId2")
    val userId2: String,

    @field:SerializedName("user1Confirmed")
    val user1Confirmed: Boolean = false,

    @field:SerializedName("user2Confirmed")
    val user2Confirmed: Boolean = true,

    @field:SerializedName("tradeConfirmed")
    val tradeConfirmed: Boolean = false
)
