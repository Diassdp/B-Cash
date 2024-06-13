package com.example.bcash.service.response

import com.example.bcash.service.response.data.TradeRequest

data class ConfirmTradeRequestResponse(
    val error: Boolean = false,
    val message: String = "",
)

data class GetTradeRequestResponse(
    val error: Boolean = false,
    val message: String = "",
    val tradeRequest: TradeRequest? = null
)

data class PostTradeRequestResponse(
    val error: Boolean = false,
    val message: String = "",
)
