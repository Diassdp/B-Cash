package com.example.bcash.service.response

import com.example.bcash.service.response.data.Profile
import com.google.gson.annotations.SerializedName

data class GetProfileResponse (
    @field:SerializedName("profile")
    val profile: Profile,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)