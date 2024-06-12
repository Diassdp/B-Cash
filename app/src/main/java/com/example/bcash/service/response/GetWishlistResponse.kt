package com.example.bcash.service.response

import com.example.bcash.service.response.data.ProductItem
import com.google.gson.annotations.SerializedName

data class GetWishlistResponse (
    @field:SerializedName("products")
    val wishlist: List<ProductItem>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class DeleteFromWishlistResponse (
    @field:SerializedName("error")
    val error: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null
)

data class AddToWishlistResponse (
    @field:SerializedName("error")
    val error: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null
    )
