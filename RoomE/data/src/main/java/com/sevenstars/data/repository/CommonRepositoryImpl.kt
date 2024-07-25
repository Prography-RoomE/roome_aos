package com.sevenstars.data.repository

import com.sevenstars.data.datasource.remote.CommonRemoteDataSource
import com.sevenstars.data.mapper.auth.SignInMapper
import com.sevenstars.domain.repository.CommonRepository
import com.sevenstars.domain.utils.RoomeResult
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val commonRemoteDataSource: CommonRemoteDataSource
): CommonRepository {

    override suspend fun getMinUpdateVersion(): RoomeResult<String> {
        val res = commonRemoteDataSource.getMinUpdateVersion()

        return if(res.code == 200){
            RoomeResult.Success(res.data!!.version)
        } else {
            RoomeResult.Failure(res.code, res.message)
        }
    }
}