package com.sevenstars.data.model.profile.info

import com.google.gson.annotations.SerializedName

data class ActivitiesDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String
)