package com.sevenstars.data.mapper.user

import com.sevenstars.data.model.user.RequestTermsDTO

object TermsAgreementMapper {
    fun mapperToRequestDTO(options: Map<String, Boolean>): RequestTermsDTO {
        return RequestTermsDTO(
            ageOverFourteen = options.get("ageOverFourteen") ?: false,
            serviceAgreement = options.get("serviceAgreement") ?: false,
            personalInfoAgreement = options.get("personalInfoAgreement") ?: false,
            marketingAgreement = options.get("marketingAgreement") ?: false
        )
    }
}