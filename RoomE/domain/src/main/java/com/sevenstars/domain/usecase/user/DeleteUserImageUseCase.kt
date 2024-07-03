package com.sevenstars.domain.usecase.user

import com.sevenstars.domain.model.user.ResponseUserInfoEntity
import com.sevenstars.domain.repository.UserRepository
import com.sevenstars.domain.utils.RoomeResult

class DeleteUserImageUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(accessToken: String): RoomeResult<Boolean> {
        return repository.deleteProfileImage("Bearer $accessToken")
    }
}