package com.sevenstars.roome.di

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import com.sevenstars.roome.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun providesUserApiClient(): UserApiClient = UserApiClient.instance

    @Provides
    fun providesGoogleSignInClient(
        app: Application
    ): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestServerAuthCode(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(app, googleSignInOption)
    }
}