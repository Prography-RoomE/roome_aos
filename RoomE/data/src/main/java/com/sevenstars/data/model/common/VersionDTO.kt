package com.sevenstars.data.model.common

import com.google.gson.annotations.SerializedName

data class VersionDTO(
    @SerializedName("version")
    val version: String
)
