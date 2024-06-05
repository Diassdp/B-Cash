package com.example.bcash.service.response

import com.google.gson.annotations.SerializedName

data class GetProfileResponse (
    @field:SerializedName("userProfile")
    val profile: Profile,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Profile(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,
)