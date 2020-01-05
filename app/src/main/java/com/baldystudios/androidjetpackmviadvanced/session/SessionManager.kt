package com.baldystudios.androidjetpackmviadvanced.session

import android.app.Application
import com.baldystudios.androidjetpackmviadvanced.persistence.AuthTokenDao

class SessionManager
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
) {


}