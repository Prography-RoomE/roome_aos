package com.sevenstars.roome.di

import com.sevenstars.data.datasource.remote.AuthRemoteDataSource
import com.sevenstars.data.datasource.remote.CommonRemoteDataSource
import com.sevenstars.data.datasource.remote.ProfileRemoteDataSource
import com.sevenstars.data.datasource.remote.UserRemoteDataSource
import com.sevenstars.data.datasourceimpl.remote.AuthRemoteDataSourceImpl
import com.sevenstars.data.datasourceimpl.remote.CommonRemoteDataSourceImpl
import com.sevenstars.data.datasourceimpl.remote.ProfileRemoteDataSourceImpl
import com.sevenstars.data.datasourceimpl.remote.UserRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsUserRemoteDataSource(userRemoteDataSourceImpl: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsProfileRemoteDataSource(profileRemoteDataSourceImpl: ProfileRemoteDataSourceImpl): ProfileRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsCommonRemoteDataSource(commonRemoteDataSourceImpl: CommonRemoteDataSourceImpl): CommonRemoteDataSource
}