package com.sevenstars.roome.di

import com.sevenstars.data.repository.UserRepositoryImpl
import com.sevenstars.data.repository.AuthRepositoryImpl
import com.sevenstars.domain.repository.UserRepository
import com.sevenstars.domain.repository.auth.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}