package com.sevenstars.domain.usecase.auth

import com.sevenstars.domain.enums.Provider
import com.sevenstars.domain.model.auth.RequestSignInEntity
import com.sevenstars.domain.model.auth.ResponseSignInEntity
import com.sevenstars.domain.repository.auth.AuthRepository
import com.sevenstars.domain.utils.RoomeResult

class SignOutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(accessToken: String): RoomeResult<Boolean> {
        return repository.signOut(accessToken)
    }
}