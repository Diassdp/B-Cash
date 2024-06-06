package com.example.bcash.model

data class SessionModel(
    val userId: String,
    val token: String,
    val name: String,
    val statusLogin: Boolean
)
