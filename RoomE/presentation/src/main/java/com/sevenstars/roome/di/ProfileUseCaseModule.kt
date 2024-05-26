package com.sevenstars.roome.di

import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.usecase.profile.DeleteProfileDataUseCase
import com.sevenstars.domain.usecase.profile.GetProfileDataUseCase
import com.sevenstars.domain.usecase.profile.GetProfileInfoUseCase
import com.sevenstars.domain.usecase.profile.SaveGenresUseCase
import com.sevenstars.domain.usecase.profile.SaveMBTIUseCase
import com.sevenstars.domain.usecase.profile.SaveRoomCountUseCase
import com.sevenstars.domain.usecase.profile.SaveStrengthsUseCase
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

    @Provides
    @Singleton
    fun provideDeleteProfileDataUseCase(
        repository: ProfileRepository
    ): DeleteProfileDataUseCase {
        return DeleteProfileDataUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveRoomCountUseCase(
        repository: ProfileRepository
    ): SaveRoomCountUseCase {
        return SaveRoomCountUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveMBTIUseCase(
        repository: ProfileRepository
    ): SaveMBTIUseCase {
        return SaveMBTIUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveGenresUseCase(
        repository: ProfileRepository
    ): SaveGenresUseCase {
        return SaveGenresUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveStrengthsUseCase(
        repository: ProfileRepository
    ): SaveStrengthsUseCase {
        return SaveStrengthsUseCase(repository = repository)
    }
}