package com.sevenstars.roome.di

import android.app.Application
import android.content.Context
import com.google.gson.GsonBuilder
import com.sevenstars.data.interceptor.TokenAuthInterceptor
import com.sevenstars.data.repository.UserPreferencesRepositoryImpl
import com.sevenstars.roome.BuildConfig
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.PrettyJsonLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenAuthInterceptor: TokenAuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenAuthInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor(PrettyJsonLogger()).apply {
        if(BuildConfig.DEBUG) setLevel(HttpLoggingInterceptor.Level.BODY)
        else setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    @Provides
    @Singleton
    fun provideTokenAuthInterceptor(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): TokenAuthInterceptor = TokenAuthInterceptor(
        application = app,
        baseUrl = BuildConfig.BASE_URL,
        userPreferencesRepositoryImpl = userPreferencesRepositoryImpl
    )
}