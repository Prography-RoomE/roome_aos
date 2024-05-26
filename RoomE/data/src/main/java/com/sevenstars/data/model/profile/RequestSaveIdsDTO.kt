package com.sevenstars.data.model.profile
import com.google.gson.annotations.SerializedName


data class RequestSaveIdsDTO(
    @SerializedName("ids") val ids: List<Int>
)