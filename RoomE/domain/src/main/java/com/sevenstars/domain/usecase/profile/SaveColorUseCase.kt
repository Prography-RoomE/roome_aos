package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.model.profile.info.Genres
import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class SaveColorUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String, selectedId: Int): RoomeResult<Boolean> {
        return repository.saveUserColor("Bearer $accessToken", selectedId)
    }
}
