package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.model.profile.ProfileInfoEntity
import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class GetProfileInfoUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String): RoomeResult<ProfileInfoEntity> {
        return repository.getDefaultProfileData("Bearer $accessToken")
    }
}