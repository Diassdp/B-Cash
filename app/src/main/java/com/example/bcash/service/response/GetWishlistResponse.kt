package com.example.bcash.service.response

import com.example.bcash.service.response.data.ProductItem
import com.google.gson.annotations.SerializedName

data class GetWishlistResponse (
    @field:SerializedName("listProduct")
    val wishlist: List<ProductItem>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)