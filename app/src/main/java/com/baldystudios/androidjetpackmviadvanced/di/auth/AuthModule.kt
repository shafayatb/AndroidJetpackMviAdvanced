package com.baldystudios.androidjetpackmviadvanced.di.auth

import android.content.SharedPreferences
import com.baldystudios.androidjetpackmviadvanced.api.auth.OpenApiAuthService
import com.baldystudios.androidjetpackmviadvanced.persistence.AccountPropertiesDao
import com.baldystudios.androidjetpackmviadvanced.persistence.AuthTokenDao
import com.baldystudios.androidjetpackmviadvanced.repository.auth.AuthRepository
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object AuthModule {

    @JvmStatic
    @AuthScope
    @Provides
    fun provideFakeApiService(retrofitBuilder: Retrofit.Builder): OpenApiAuthService {
        return retrofitBuilder
            .build()
            .create(OpenApiAuthService::class.java)
    }

    @JvmStatic
    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        openApiAuthService: OpenApiAuthService,
        sharedPreferences: SharedPreferences,
        sharedPrefsEditor: SharedPreferences.Editor
    ): AuthRepository {
        return AuthRepository(
            authTokenDao,
            accountPropertiesDao,
            openApiAuthService,
            sessionManager,
            sharedPreferences,
            sharedPrefsEditor
        )
    }

}