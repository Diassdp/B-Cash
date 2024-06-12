package com.example.bcash.service.response

import com.example.bcash.service.response.data.ProductItem
import com.google.gson.annotations.SerializedName

data class GetProductResponse (
    @field:SerializedName("products")
    val listProduct: List<ProductItem>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class AddProductResponse (
    @field:SerializedName(value = "error")
    val error: Boolean? = true,

    @field:SerializedName(value = "message")
    val message: String? = null
)

