package com.sevenstars.domain.usecase.user

import com.sevenstars.domain.model.user.ResponseUserInfoEntity
import com.sevenstars.domain.repository.UserRepository
import com.sevenstars.domain.utils.RoomeResult

class PostUserImageUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(accessToken: String, realPath: String): RoomeResult<String> {
        return repository.postProfileImage("Bearer $accessToken", realPath)
    }
}