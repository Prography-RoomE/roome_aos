package com.sevenstars.domain.repository

import com.sevenstars.domain.model.BaseEntity
import com.sevenstars.domain.model.user.ResponseUserInfoEntity
import com.sevenstars.domain.utils.RoomeResult

interface UserRepository {
    suspend fun getUserInfo(accessToken: String
    ): RoomeResult<ResponseUserInfoEntity>

    suspend fun validationNick(accessToken: String, nickname: String
    ): RoomeResult<BaseEntity>

    suspend fun saveNick(accessToken: String, nickname: String
    ): RoomeResult<BaseEntity>

    suspend fun saveTermsAgreement(accessToken: String, options: Map<String, Boolean>
    ): RoomeResult<BaseEntity>
}