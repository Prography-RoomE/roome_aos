package com.sevenstars.domain.usecase.user

import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.repository.UserRepository
import com.sevenstars.domain.utils.RoomeResult

class GetUserProfileDataUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(nickname: String): RoomeResult<SavedProfileData> {
        return repository.getUserProfile(nickname)
    }
}