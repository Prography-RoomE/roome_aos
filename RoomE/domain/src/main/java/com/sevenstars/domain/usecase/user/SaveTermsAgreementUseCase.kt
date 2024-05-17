package com.sevenstars.domain.usecase.user

import com.sevenstars.domain.model.BaseEntity
import com.sevenstars.domain.repository.UserRepository
import com.sevenstars.domain.utils.RoomeResult

class SaveTermsAgreementUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(
        accessToken: String,
        ageOverFourteen: Boolean,
        serviceAgreement: Boolean,
        personalInfoAgreement: Boolean,
        marketingAgreement: Boolean
    ): RoomeResult<BaseEntity> {
        val queryParams = mapOf(
            "ageOverFourteen" to ageOverFourteen,
            "serviceAgreement" to serviceAgreement,
            "personalInfoAgreement" to personalInfoAgreement,
            "marketingAgreement" to marketingAgreement
        )

        return repository.saveTermsAgreement("Bearer $accessToken", queryParams)
    }
}