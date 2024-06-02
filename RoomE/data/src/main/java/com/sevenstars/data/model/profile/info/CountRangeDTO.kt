package com.sevenstars.data.model.profile.info
import com.google.gson.annotations.SerializedName


data class CountRangeDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("maxCount") val maxCount: Int,
    @SerializedName("minCount") val minCount: Int,
    @SerializedName("title") val title: String
)