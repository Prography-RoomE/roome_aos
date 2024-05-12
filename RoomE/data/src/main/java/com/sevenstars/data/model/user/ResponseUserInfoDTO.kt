package com.sevenstars.data.model.user

import com.google.gson.annotations.SerializedName

data class ResponseUserInfoDTO(
    @SerializedName("email")
    val email: String,
    @SerializedName("nickname")
    val nickname: String
)
