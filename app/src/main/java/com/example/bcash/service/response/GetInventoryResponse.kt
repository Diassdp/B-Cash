package com.example.bcash.service.response

import com.google.gson.annotations.SerializedName

data class GetInventoryResponse (
    @field:SerializedName("listProduct")
    val listProduct: List<ProductItem>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)