package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class GetProfileDataUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String): RoomeResult<SavedProfileData> {
        return repository.getProfileData("Bearer $accessToken")
    }
}