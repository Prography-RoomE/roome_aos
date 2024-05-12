package com.sevenstars.data.repository

import com.sevenstars.data.datasource.remote.UserRemoteDataSource
import com.sevenstars.data.mapper.user.UserInfoMapper
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
}