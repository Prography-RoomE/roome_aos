package com.sevenstars.domain.repository

import com.sevenstars.domain.model.user.ResponseUserInfoEntity
import com.sevenstars.domain.utils.RoomeResult

interface UserRepository {
    suspend fun getUserInfo(accessToken: String
    ): RoomeResult<ResponseUserInfoEntity>
}