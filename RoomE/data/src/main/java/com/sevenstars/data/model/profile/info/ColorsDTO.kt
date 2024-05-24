package com.sevenstars.data.model.profile.info

import com.google.gson.annotations.SerializedName

data class ColorsDTO(
    @SerializedName("direction") val direction: String,
    @SerializedName("endColor") val endColor: String,
    @SerializedName("id") val id: Int,
    @SerializedName("mode") val mode: String,
    @SerializedName("shape") val shape: String,
    @SerializedName("startColor") val startColor: String,
    @SerializedName("title") val title: String
)