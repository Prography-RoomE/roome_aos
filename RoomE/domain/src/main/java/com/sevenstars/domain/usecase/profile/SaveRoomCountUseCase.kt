package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class SaveRoomCountUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String, count: Int, isPlusEnabled: Boolean): RoomeResult<Boolean> {
        return repository.saveUserRoomCount("Bearer $accessToken", count, isPlusEnabled)
    }
}
