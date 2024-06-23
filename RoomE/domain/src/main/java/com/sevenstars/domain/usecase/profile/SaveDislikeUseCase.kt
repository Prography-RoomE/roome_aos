package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class SaveDislikeUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String, dislikeFactors: List<Int>): RoomeResult<Boolean> {
        return repository.saveUserDislikeFactors("Bearer $accessToken", dislikeFactors)
    }
}
