package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class DeleteProfileDataUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String): RoomeResult<Boolean> {
        return repository.deleteProfileData("Bearer $accessToken")
    }
}