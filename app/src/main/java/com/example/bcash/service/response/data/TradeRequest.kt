package com.example.bcash.service.response.data

data class TradeRequest(
    val id: Int,
    val itemId1: String,
    val itemId2: String,
    val userId1: String,
    val userId2: String,
    val user1Confirmed: Boolean = false,
    val user2Confirmed: Boolean = false,
    val tradeConfirmed: Boolean = false
)
