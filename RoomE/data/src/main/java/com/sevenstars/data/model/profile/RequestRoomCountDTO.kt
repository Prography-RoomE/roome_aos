package com.sevenstars.data.model.profile

import com.google.gson.annotations.SerializedName

data class RequestRoomCountDTO(
    @SerializedName("count") val count: Int,
    @SerializedName("isPlusEnabled") val isPlusEnabled: Boolean
)
