package com.sevenstars.roome.di

import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.usecase.profile.DeleteProfileDataUseCase
import com.sevenstars.domain.usecase.profile.GetProfileDataUseCase
import com.sevenstars.domain.usecase.profile.GetProfileInfoUseCase
import com.sevenstars.domain.usecase.profile.SaveActivityUseCase
import com.sevenstars.domain.usecase.profile.SaveColorUseCase
import com.sevenstars.domain.usecase.profile.SaveDeviceOrLockUseCase
import com.sevenstars.domain.usecase.profile.SaveDislikeUseCase
import com.sevenstars.domain.usecase.profile.SaveGenresUseCase
import com.sevenstars.domain.usecase.profile.SaveHintUsageUseCase
import com.sevenstars.domain.usecase.profile.SaveHorrorUseCase
import com.sevenstars.domain.usecase.profile.SaveImportantUseCase
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

    @Provides
    @Singleton
    fun provideSaveImportantUseCase(
        repository: ProfileRepository
    ): SaveImportantUseCase {
        return SaveImportantUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveHorrorUseCase(
        repository: ProfileRepository
    ): SaveHorrorUseCase {
        return SaveHorrorUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveHintUsageUseCase(
        repository: ProfileRepository
    ): SaveHintUsageUseCase {
        return SaveHintUsageUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveDeviceOrLockUseCase(
        repository: ProfileRepository
    ): SaveDeviceOrLockUseCase {
        return SaveDeviceOrLockUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveActivityUseCase(
        repository: ProfileRepository
    ): SaveActivityUseCase {
        return SaveActivityUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveDislikeUseCase(
        repository: ProfileRepository
    ): SaveDislikeUseCase {
        return SaveDislikeUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveColorUseCase(
        repository: ProfileRepository
    ): SaveColorUseCase {
        return SaveColorUseCase(repository = repository)
    }
}