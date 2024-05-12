package com.sevenstars.data.mapper.user

import com.sevenstars.data.model.user.ResponseUserInfoDTO
import com.sevenstars.domain.model.user.ResponseUserInfoEntity

object UserInfoMapper {

    fun mapperToResponseEntity(item: ResponseUserInfoDTO): ResponseUserInfoEntity {
        return item.run {
            ResponseUserInfoEntity(email, nickname)
        }
    }
}