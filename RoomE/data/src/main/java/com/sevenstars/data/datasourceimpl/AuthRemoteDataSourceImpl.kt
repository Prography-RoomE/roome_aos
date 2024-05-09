package com.sevenstars.data.datasourceimpl

import com.sevenstars.data.datasource.remote.AuthRemoteDataSource
import com.sevenstars.data.model.auth.RequestSignInDTO
import com.sevenstars.data.model.auth.ResponseSignInDTO
import com.sevenstars.data.service.auth.AuthService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService
): AuthRemoteDataSource {
    override suspend fun signIn(body: RequestSignInDTO): ResponseSignInDTO
        = authService.signIn(body).data
}