package com.sevenstars.data.model.user

data class RequestTermsDTO(
    val ageOverFourteen: Boolean,
    val serviceAgreement: Boolean,
    val personalInfoAgreement: Boolean,
    val marketingAgreement: Boolean
)
