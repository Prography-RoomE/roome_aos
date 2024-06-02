package com.sevenstars.data.model.profile

import com.google.gson.annotations.SerializedName

data class RequestRoomCountRangeDTO(
    @SerializedName("minCount")
    val minCount: Int,
    @SerializedName("maxCount")
    val maxCount: Int
)
