package com.sevenstars.domain.usecase.auth

import com.sevenstars.domain.enums.Provider
import com.sevenstars.domain.model.auth.RequestSignInEntity
import com.sevenstars.domain.model.auth.ResponseSignInEntity
import com.sevenstars.domain.repository.auth.AuthRepository
import com.sevenstars.domain.utils.RoomeResult

class UnlinkUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(accessToken: String, provider: String, code: String?): RoomeResult<Boolean> {
        return repository.unlink(accessToken, provider, code)
    }
}