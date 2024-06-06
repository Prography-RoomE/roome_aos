package com.sevenstars.data.repository

import com.sevenstars.data.datasource.remote.AuthRemoteDataSource
import com.sevenstars.data.mapper.auth.SignInMapper
import com.sevenstars.data.model.auth.RequestRefreshTokenDTO
import com.sevenstars.data.model.auth.RequestUnlinkDTO
import com.sevenstars.domain.model.auth.RequestSignInEntity
import com.sevenstars.domain.model.auth.ResponseSignInEntity
import com.sevenstars.domain.repository.auth.AuthRepository
import com.sevenstars.domain.utils.RoomeResult
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
): AuthRepository {

    override suspend fun signIn(body: RequestSignInEntity): RoomeResult<ResponseSignInEntity> {
        val res = authRemoteDataSource.signIn(SignInMapper.mapperToRequestDto(body))

        return if(res.code == 200){
            RoomeResult.Success(SignInMapper.mapperToResponseEntity(res.data!!))
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun unlink(
        accessToken: String,
        provider: String,
        code: String?
    ): RoomeResult<Boolean> {
        val res = authRemoteDataSource.unlink(accessToken, RequestUnlinkDTO(provider, code))

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun signOut(accessToken: String): RoomeResult<Boolean> {
        val res = authRemoteDataSource.signOut(accessToken)

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun refreshToken(refreshToken: String): RoomeResult<ResponseSignInEntity> {
        val res = authRemoteDataSource.refreshToken(RequestRefreshTokenDTO(refreshToken))

        return if(res.code == 200){
            RoomeResult.Success(SignInMapper.mapperToResponseEntity(res.data!!))
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }
}