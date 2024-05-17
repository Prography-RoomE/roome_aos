package com.sevenstars.domain.model.user

import com.sevenstars.domain.enums.UserState

data class ResponseUserInfoEntity(
    val state: UserState,
    val email: String,
    val nickname: String
)
