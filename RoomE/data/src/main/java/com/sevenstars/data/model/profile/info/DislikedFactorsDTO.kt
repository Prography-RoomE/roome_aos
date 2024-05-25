package com.sevenstars.data.model.profile.info

import com.google.gson.annotations.SerializedName

data class DislikedFactorsDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String
)