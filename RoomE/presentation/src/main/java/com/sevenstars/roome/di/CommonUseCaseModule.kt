package com.sevenstars.roome.di

import com.sevenstars.domain.repository.CommonRepository
import com.sevenstars.domain.repository.auth.AuthRepository
import com.sevenstars.domain.usecase.auth.SignInUseCase
import com.sevenstars.domain.usecase.auth.SignOutUseCase
import com.sevenstars.domain.usecase.auth.UnlinkUseCase
import com.sevenstars.domain.usecase.common.GetMinUpdateVersionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonUseCaseModule {

    @Provides
    @Singleton
    fun provideGetMinUpdateVersionUseCase(
        repository: CommonRepository
    ): GetMinUpdateVersionUseCase {
        return GetMinUpdateVersionUseCase(repository = repository)
    }
}