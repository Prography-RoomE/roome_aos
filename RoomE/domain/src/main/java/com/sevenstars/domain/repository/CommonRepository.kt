package com.sevenstars.domain.repository

import com.sevenstars.domain.utils.RoomeResult

interface CommonRepository {

    suspend fun getMinUpdateVersion(): RoomeResult<String>
}