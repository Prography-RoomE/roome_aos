package com.sevenstars.data.model.auth

data class RequestUnlinkDTO(
    val provider: String,
    val code: String?
)
