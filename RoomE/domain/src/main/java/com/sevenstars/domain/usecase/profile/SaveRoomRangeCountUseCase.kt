package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class SaveRoomRangeCountUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String, minCount: Int, maxCount: Int): RoomeResult<Boolean> {
        return repository.saveUserRoomCountRange("Bearer $accessToken", minCount, maxCount)
    }
}
