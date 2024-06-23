package com.sevenstars.domain.usecase.profile

import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult

class SaveDeviceOrLockUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(accessToken: String, selectedId: Int): RoomeResult<Boolean> {
        return repository.saveUserDeviceLockPreference("Bearer $accessToken", selectedId)
    }
}
