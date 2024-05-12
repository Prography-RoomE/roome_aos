package com.sevenstars.data.model

import com.google.gson.annotations.SerializedName


data class BaseResponse<T>(
//    val response: Response<RoomeResponse<T>>
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T?
)