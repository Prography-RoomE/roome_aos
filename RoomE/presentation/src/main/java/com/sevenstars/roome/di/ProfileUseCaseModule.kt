package com.sevenstars.roome.di

import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.usecase.profile.GetProfileDataUseCase
import com.sevenstars.domain.usecase.profile.GetProfileInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileUseCaseModule {

    @Provides
    @Singleton
    fun provideGetProfileInfoUseCase(
        repository: ProfileRepository
    ): GetProfileInfoUseCase {
        return GetProfileInfoUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideGetProfileDataUseCase(
        repository: ProfileRepository
    ): GetProfileDataUseCase {
        return GetProfileDataUseCase(repository = repository)
    }

}