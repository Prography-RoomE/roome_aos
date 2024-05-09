package com.sevenstars.data.mapper.auth

import com.sevenstars.data.model.auth.RequestSignInDTO
import com.sevenstars.data.model.auth.ResponseSignInDTO
import com.sevenstars.domain.model.auth.RequestSignInEntity
import com.sevenstars.domain.model.auth.ResponseSignInEntity

object SignInMapper {
    fun mapperToRequestDto(item: RequestSignInEntity): RequestSignInDTO {
        return item.run {
            RequestSignInDTO(provider, code, idToken)
        }
    }

    fun mapperToResponseEntity(item: ResponseSignInDTO): ResponseSignInEntity {
        return item.run {
            ResponseSignInEntity(accessToken, refreshToken)
        }
    }
}