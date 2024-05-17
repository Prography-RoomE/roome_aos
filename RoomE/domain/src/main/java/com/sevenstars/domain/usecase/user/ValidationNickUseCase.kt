package com.sevenstars.domain.usecase.user

import com.sevenstars.domain.model.BaseEntity
import com.sevenstars.domain.repository.UserRepository
import com.sevenstars.domain.utils.RoomeResult

class ValidationNickUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(accessToken: String, nickname: String): RoomeResult<BaseEntity> {
        return repository.validationNick("Bearer $accessToken", nickname)
    }
}