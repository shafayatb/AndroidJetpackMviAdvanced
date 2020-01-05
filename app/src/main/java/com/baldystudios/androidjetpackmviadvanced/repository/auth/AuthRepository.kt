package com.baldystudios.androidjetpackmviadvanced.repository.auth

import androidx.lifecycle.LiveData
import com.baldystudios.androidjetpackmviadvanced.api.auth.OpenApiAuthService
import com.baldystudios.androidjetpackmviadvanced.api.auth.network_responses.LoginResponse
import com.baldystudios.androidjetpackmviadvanced.api.auth.network_responses.RegistrationResponse
import com.baldystudios.androidjetpackmviadvanced.persistence.AccountPropertiesDao
import com.baldystudios.androidjetpackmviadvanced.persistence.AuthTokenDao
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
) {

    fun testLoginRequest(
        email: String,
        password: String
    ): LiveData<GenericApiResponse<LoginResponse>> {
        return openApiAuthService.login(email, password)
    }

    fun testRegistrationRequest(userMap: Map<String, String>): LiveData<GenericApiResponse<RegistrationResponse>> {
        return openApiAuthService.register(userMap)
    }

}