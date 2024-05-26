package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.model.profile.info.Genres
import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class SaveStrengthsUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String, strengths: List<Int>): RoomeResult<Boolean> {
        return repository.saveUserStrengths("Bearer $accessToken", strengths)
    }
}
