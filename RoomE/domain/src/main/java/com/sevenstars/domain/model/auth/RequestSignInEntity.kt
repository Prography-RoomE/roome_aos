package com.sevenstars.domain.model.auth

data class RequestSignInEntity(
    val provider: String,
    val code: String?,
    val idToken: String
)
