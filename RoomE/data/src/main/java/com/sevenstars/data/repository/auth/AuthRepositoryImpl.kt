package com.sevenstars.data.repository.auth

import com.sevenstars.data.datasource.remote.AuthRemoteDataSource
import com.sevenstars.data.mapper.auth.SignInMapper
import com.sevenstars.domain.model.auth.RequestSignInEntity
import com.sevenstars.domain.model.auth.ResponseSignInEntity
import com.sevenstars.domain.repository.auth.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
): AuthRepository {

    override suspend fun signIn(body: RequestSignInEntity): Result<ResponseSignInEntity> {
        val response =  authRemoteDataSource.signIn(SignInMapper.mapperToRequestDto(body))

        return kotlin.runCatching {
            SignInMapper.mapperToResponseEntity(response)
        }
    }
}