package com.sevenstars.domain.repository

import com.sevenstars.domain.model.profile.ProfileInfoEntity
import com.sevenstars.domain.utils.RoomeResult

interface ProfileRepository {
    suspend fun getDefaultProfileData(
        accessToken: String
    ): RoomeResult<ProfileInfoEntity>
}