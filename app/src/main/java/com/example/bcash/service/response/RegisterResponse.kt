package com.example.bcash.service.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @field:SerializedName("error")
    val error: Boolean? = true,
    @field:SerializedName("message")
    val message: String? = null,
)