package com.sevenstars.data.datasource.remote

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.auth.RequestSignInDTO
import com.sevenstars.data.model.auth.ResponseSignInDTO

interface AuthRemoteDataSource {
    suspend fun signIn(body: RequestSignInDTO): BaseResponse<ResponseSignInDTO>
}