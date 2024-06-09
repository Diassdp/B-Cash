package com.example.bcash.service.response.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductItem(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("photoUrl")
    val photo: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("condition")
    val condition: String? = null,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("userId")
    val userId: String,
): Parcelable