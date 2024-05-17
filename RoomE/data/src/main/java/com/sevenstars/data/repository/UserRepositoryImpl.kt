package com.sevenstars.data.repository

import com.sevenstars.data.datasource.remote.UserRemoteDataSource
import com.sevenstars.data.mapper.user.UserInfoMapper
import com.sevenstars.domain.model.BaseEntity
import com.sevenstars.domain.model.user.ResponseUserInfoEntity
import com.sevenstars.domain.repository.UserRepository
import com.sevenstars.domain.utils.RoomeResult

import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
): UserRepository {

    override suspend fun getUserInfo(accessToken: String): RoomeResult<ResponseUserInfoEntity> {

        val res = userRemoteDataSource.getUserInfo(accessToken)
        return if(res.code == 200){
            RoomeResult.Success(UserInfoMapper.mapperToResponseEntity(res.data!!))
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun validationNick(
        accessToken: String,
        nickname: String
    ): RoomeResult<BaseEntity> {
        val res = userRemoteDataSource.validationNick(accessToken, nickname)
        return if(res.code == 200){
            RoomeResult.Success(BaseEntity(res.code, res.message))
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveNick(accessToken: String, nickname: String): RoomeResult<BaseEntity> {
        val res = userRemoteDataSource.saveNick(accessToken, nickname)
        return if(res.code == 200){
            RoomeResult.Success(BaseEntity(res.code, res.message))
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveTermsAgreement(
        accessToken: String,
        options: Map<String, Boolean>
    ): RoomeResult<BaseEntity> {
        val res = userRemoteDataSource.saveTermsAgreement(accessToken, options)
        return if(res.code == 200){
            RoomeResult.Success(BaseEntity(res.code, res.message))
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }
}