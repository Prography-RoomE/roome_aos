package com.sevenstars.data.model.user

import com.google.gson.annotations.SerializedName

data class ResponsePostImageDTO (
    @SerializedName("imageUrl")
    val imageUrl: String
)