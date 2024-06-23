package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class SaveHintUsageUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String, hintUsage: Int): RoomeResult<Boolean> {
        return repository.saveUserHintUsagePreference("Bearer $accessToken", hintUsage)
    }
}
