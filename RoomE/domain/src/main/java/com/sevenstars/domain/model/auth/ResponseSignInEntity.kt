package com.sevenstars.domain.model.auth

data class ResponseSignInEntity(
    val accessToken: String,
    val refreshToken: String
)
