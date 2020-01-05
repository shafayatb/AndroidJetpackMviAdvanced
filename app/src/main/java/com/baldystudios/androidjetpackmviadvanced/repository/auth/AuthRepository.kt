package com.baldystudios.androidjetpackmviadvanced.repository.auth

import com.baldystudios.androidjetpackmviadvanced.api.auth.OpenApiAuthService
import com.baldystudios.androidjetpackmviadvanced.persistence.AccountPropertiesDao
import com.baldystudios.androidjetpackmviadvanced.persistence.AuthTokenDao
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager

class AuthRepository
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
){



}