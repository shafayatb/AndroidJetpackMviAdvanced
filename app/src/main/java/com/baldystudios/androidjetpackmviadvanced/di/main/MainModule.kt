package com.baldystudios.androidjetpackmviadvanced.di.main

import com.baldystudios.androidjetpackmviadvanced.api.main.OpenApiMainService
import com.baldystudios.androidjetpackmviadvanced.persistence.AccountPropertiesDao
import com.baldystudios.androidjetpackmviadvanced.persistence.AppDatabase
import com.baldystudios.androidjetpackmviadvanced.persistence.BlogPostDao
import com.baldystudios.androidjetpackmviadvanced.repository.main.AccountRepository
import com.baldystudios.androidjetpackmviadvanced.repository.main.BlogRepository
import com.baldystudios.androidjetpackmviadvanced.repository.main.CreateBlogRepository
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object MainModule {

    @JvmStatic
    @MainScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): OpenApiMainService {
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideAccountRepository(
        openApiMainService: OpenApiMainService,
        accountPropertiesDao: AccountPropertiesDao,
        sessionManager: SessionManager
    ): AccountRepository {
        return AccountRepository(
            openApiMainService,
            accountPropertiesDao,
            sessionManager
        )
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideBlogPostDao(db: AppDatabase): BlogPostDao {
        return db.getBlogPostDao()
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideBlogRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ): BlogRepository {
        return BlogRepository(openApiMainService, blogPostDao, sessionManager)
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideCreateBlogRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ): CreateBlogRepository {
        return CreateBlogRepository(openApiMainService, blogPostDao, sessionManager)
    }

}