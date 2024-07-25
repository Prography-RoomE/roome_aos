package com.sevenstars.domain.usecase.common

import com.sevenstars.domain.repository.CommonRepository
import com.sevenstars.domain.utils.RoomeResult

class GetMinUpdateVersionUseCase(private val repository: CommonRepository) {
    suspend operator fun invoke(): RoomeResult<String> {
        return repository.getMinUpdateVersion()
    }
}