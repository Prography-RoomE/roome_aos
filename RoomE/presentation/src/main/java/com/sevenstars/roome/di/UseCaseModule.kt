package com.sevenstars.roome.di

import com.sevenstars.domain.repository.auth.AuthRepository
import com.sevenstars.domain.usecase.SignInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideSignInUseCase(
        repository: AuthRepository
    ): SignInUseCase{
        return SignInUseCase(repository = repository)
    }
}