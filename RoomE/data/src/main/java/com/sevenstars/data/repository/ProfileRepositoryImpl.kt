package com.sevenstars.data.repository

import com.sevenstars.data.datasource.remote.ProfileRemoteDataSource
import com.sevenstars.data.mapper.profile.ProfileInfoMapper
import com.sevenstars.domain.model.profile.ProfileInfoEntity
import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource
): ProfileRepository {

    override suspend fun getProfileInfo(accessToken: String): RoomeResult<ProfileInfoEntity> {
        val res = profileRemoteDataSource.getProfileInfo(accessToken)

        return if(res.code == 200){
            RoomeResult.Success(ProfileInfoMapper.mapperToResponseEntity(res.data!!))
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }
}