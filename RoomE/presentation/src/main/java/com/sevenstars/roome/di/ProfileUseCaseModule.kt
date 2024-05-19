package com.sevenstars.roome.di

import com.sevenstars.domain.repository.UserRepository
import com.sevenstars.domain.usecase.user.GetUserInfoUseCase
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
    fun provideGetUserInfoUseCase(
        repository: UserRepository
    ): GetUserInfoUseCase {
        return GetUserInfoUseCase(repository = repository)
    }

}