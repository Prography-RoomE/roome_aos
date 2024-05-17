package com.sevenstars.roome.di

import com.sevenstars.domain.repository.UserRepository
import com.sevenstars.domain.usecase.user.GetUserInfoUseCase
import com.sevenstars.domain.usecase.user.SaveNickUseCase
import com.sevenstars.domain.usecase.user.SaveTermsAgreementUseCase
import com.sevenstars.domain.usecase.user.ValidationNickUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserUseCaseModule {

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(
        repository: UserRepository
    ): GetUserInfoUseCase {
        return GetUserInfoUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideValidationNickUseCase(
        repository: UserRepository
    ): ValidationNickUseCase {
        return ValidationNickUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveNickUseCase(
        repository: UserRepository
    ): SaveNickUseCase {
        return SaveNickUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveTermsAgreementUseCase(
        repository: UserRepository
    ): SaveTermsAgreementUseCase {
        return SaveTermsAgreementUseCase(repository = repository)
    }
}