package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class SaveGenresUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String, genres: List<Int>): RoomeResult<Boolean> {
        return repository.saveUserPreferredGenres("Bearer $accessToken", genres)
    }
}
