package com.sevenstars.data.repository

import com.sevenstars.data.datasource.remote.ProfileRemoteDataSource
import com.sevenstars.data.mapper.profile.ProfileInfoMapper
import com.sevenstars.data.mapper.profile.ProfileMapper
import com.sevenstars.data.model.profile.RequestMbtiDTO
import com.sevenstars.data.model.profile.RequestRoomCountDTO
import com.sevenstars.data.model.profile.RequestSaveIdsDTO
import com.sevenstars.data.model.profile.info.RequestSaveIdDTO
import com.sevenstars.domain.model.profile.ProfileInfoEntity
import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.utils.RoomeResult
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource
): ProfileRepository {

    override suspend fun getDefaultProfileData(accessToken: String): RoomeResult<ProfileInfoEntity> {
        val res = profileRemoteDataSource.getDefaultProfileData(accessToken)

        return if(res.code == 200){
            RoomeResult.Success(ProfileInfoMapper.mapperToResponseEntity(res.data!!))
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun getProfileData(accessToken: String): RoomeResult<SavedProfileData> {
        val res = profileRemoteDataSource.getProfileData(accessToken)

        return if(res.code == 200){
            RoomeResult.Success(ProfileMapper.mapperToResponseEntity(res.data!!))
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun deleteProfileData(accessToken: String): RoomeResult<Boolean> {
        val res = profileRemoteDataSource.deleteProfileData(accessToken)

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveUserStrengths(
        accessToken: String,
        body: List<Int>
    ): RoomeResult<Boolean> {
        val res = profileRemoteDataSource.saveUserStrengths(accessToken, RequestSaveIdsDTO(body))

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveUserImportantFactors(
        accessToken: String,
        body: List<Int>
    ): RoomeResult<Boolean> {
        val res = profileRemoteDataSource.saveUserImportantFactors(accessToken, RequestSaveIdsDTO(body))

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveUserDislikeFactors(
        accessToken: String,
        body: List<Int>
    ): RoomeResult<Boolean> {
        val res = profileRemoteDataSource.saveUserDislikeFactors(accessToken, RequestSaveIdsDTO(body))

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveUserRoomCount(
        accessToken: String,
        count: Int,
        isPlusEnabled: Boolean
    ): RoomeResult<Boolean> {
        val res = profileRemoteDataSource.saveUserRoomCount(accessToken, RequestRoomCountDTO(count, isPlusEnabled))

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveUserPreferredGenres(
        accessToken: String,
        body: List<Int>
    ): RoomeResult<Boolean> {
        val res = profileRemoteDataSource.saveUserPreferredGenres(accessToken, RequestSaveIdsDTO(body))

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveUserMbti(
        accessToken: String,
        body: String
    ): RoomeResult<Boolean> {
        val res = profileRemoteDataSource.saveUserMbti(accessToken, RequestMbtiDTO(body))

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveUserHorrorThemePos(
        accessToken: String,
        body: Int
    ): RoomeResult<Boolean> {
        val res = profileRemoteDataSource.saveUserHorrorThemePos(accessToken, RequestSaveIdDTO(body))

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveUserHintUsagePreference(
        accessToken: String,
        body: Int
    ): RoomeResult<Boolean> {
        val res = profileRemoteDataSource.saveUserHintUsagePreference(accessToken, RequestSaveIdDTO(body))

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveUserDeviceLockPreference(
        accessToken: String,
        body: Int
    ): RoomeResult<Boolean> {
        val res = profileRemoteDataSource.saveUserDeviceLockPreference(accessToken, RequestSaveIdDTO(body))

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveUserColor(accessToken: String, body: Int): RoomeResult<Boolean> {
        val res = profileRemoteDataSource.saveUserColor(accessToken, RequestSaveIdDTO(body))

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }

    override suspend fun saveUserActivity(accessToken: String, body: Int): RoomeResult<Boolean> {
        val res = profileRemoteDataSource.saveUserActivity(accessToken, RequestSaveIdDTO(body))

        return if(res.code == 200){
            RoomeResult.Success(true)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }
}