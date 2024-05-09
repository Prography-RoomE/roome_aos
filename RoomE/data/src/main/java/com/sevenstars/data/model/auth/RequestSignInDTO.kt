package com.sevenstars.data.model.auth

data class RequestSignInDTO(
    val provider: String,
    val code: String?,
    val idToken: String
)
