package com.baldystudios.androidjetpackmviadvanced.repository.main

import com.baldystudios.androidjetpackmviadvanced.api.main.OpenApiMainService
import com.baldystudios.androidjetpackmviadvanced.persistence.BlogPostDao
import com.baldystudios.androidjetpackmviadvanced.repository.JobManager
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import javax.inject.Inject

class BlogRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): JobManager("BlogRepository"){

    private val TAG: String = "AppDebug"

}