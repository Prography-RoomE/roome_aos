package com.sevenstars.domain.usecase.auth

import com.sevenstars.domain.repository.auth.AuthRepository
import com.sevenstars.domain.utils.RoomeResult

class UnlinkUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(accessToken: String, provider: String, code: String?, reason: String, content: String?): RoomeResult<Boolean> {
        return repository.unlink(accessToken, provider, code, reason, content ?: "")
    }
}