package com.sevenstars.roome.di

import com.sevenstars.domain.repository.auth.AuthRepository
import com.sevenstars.domain.usecase.auth.SignInUseCase
import com.sevenstars.domain.usecase.auth.SignOutUseCase
import com.sevenstars.domain.usecase.auth.UnlinkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {

    @Provides
    @Singleton
    fun provideSignInUseCase(
        repository: AuthRepository
    ): SignInUseCase {
        return SignInUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSignOutUseCase(
        repository: AuthRepository
    ): SignOutUseCase {
        return SignOutUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideUnlinkUseCase(
        repository: AuthRepository
    ): UnlinkUseCase {
        return UnlinkUseCase(repository = repository)
    }
}