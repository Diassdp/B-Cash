package com.example.bcash.service.response

import com.example.bcash.service.response.data.ProductItem
import com.google.gson.annotations.SerializedName

data class GetInventoryResponse (
    @field:SerializedName("products")
    val inventory: List<ProductItem>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class DeleteInventoryResponse (
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)