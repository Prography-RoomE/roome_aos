package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.model.profile.info.Genres
import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class SaveHorrorUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String, horrorPos: Int): RoomeResult<Boolean> {
        return repository.saveUserHorrorThemePos("Bearer $accessToken", horrorPos)
    }
}
