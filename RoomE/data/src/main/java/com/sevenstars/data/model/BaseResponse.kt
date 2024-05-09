package com.sevenstars.data.model

import com.google.gson.annotations.SerializedName


data class BaseResponse<T>(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T
)
