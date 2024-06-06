package com.example.bcash.service.response

import com.example.bcash.service.response.data.TradeRequest

data class TradeRequestResponse(
    val error: Boolean = false,
    val message: String = "",
    val tradeRequest: TradeRequest? = null
)
