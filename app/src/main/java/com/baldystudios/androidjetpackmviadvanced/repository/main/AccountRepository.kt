package com.baldystudios.androidjetpackmviadvanced.repository.main

import android.util.Log
import com.baldystudios.androidjetpackmviadvanced.api.main.OpenApiMainService
import com.baldystudios.androidjetpackmviadvanced.persistence.AccountPropertiesDao
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import kotlinx.coroutines.Job
import javax.inject.Inject

class AccountRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val accountPropertiesDao: AccountPropertiesDao,
    val sessionManager: SessionManager
){

    private val TAG: String = "AppDebug"

    private var repositoryJob: Job? = null

    fun cancelActiveJobs(){
        Log.d(TAG, "AccountRepository: cancelling active jobs...")
    }

}