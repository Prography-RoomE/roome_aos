package com.sevenstars.data.model.profile

import com.google.gson.annotations.SerializedName

data class RequestMbtiDTO(
    @SerializedName("mbti") val mbti: String
)
