package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class SaveImportantUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String, importantFactors: List<Int>): RoomeResult<Boolean> {
        return repository.saveUserImportantFactors("Bearer $accessToken", importantFactors)
    }
}
