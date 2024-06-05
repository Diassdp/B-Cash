package com.example.bcash.service.response

import com.google.gson.annotations.SerializedName

class AddProductResponse (
    @field:SerializedName(value = "error")
    val error: Boolean? = true,

    @field:SerializedName(value = "message")
    val message: String? = null
)