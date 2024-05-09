package com.sevenstars.data.model.auth

import com.google.gson.annotations.SerializedName

data class ResponseSignInDTO(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
