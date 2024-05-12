package com.sevenstars.domain.usecase.user

import com.sevenstars.domain.model.user.ResponseUserInfoEntity
import com.sevenstars.domain.repository.UserRepository
import com.sevenstars.domain.utils.RoomeResult

class GetUserInfoUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(accessToken: String): RoomeResult<ResponseUserInfoEntity> {
        return repository.getUserInfo("Bearer $accessToken")
    }
}