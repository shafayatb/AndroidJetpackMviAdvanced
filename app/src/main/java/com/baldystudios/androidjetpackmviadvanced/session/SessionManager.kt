package com.baldystudios.androidjetpackmviadvanced.session

import android.app.Application
import com.baldystudios.androidjetpackmviadvanced.persistence.AuthTokenDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
) {


}