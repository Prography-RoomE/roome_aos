package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class SaveMBTIUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String, mbti: String): RoomeResult<Boolean> {
        return repository.saveUserMbti("Bearer $accessToken", mbti)
    }
}
