package com.baldystudios.androidjetpackmviadvanced.di.main

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.baldystudios.androidjetpackmviadvanced.fragments.auth.AuthFragmentFactory
import com.baldystudios.androidjetpackmviadvanced.fragments.main.account.AccountFragmentFactory
import com.baldystudios.androidjetpackmviadvanced.fragments.main.blog.BlogFragmentFactory
import com.baldystudios.androidjetpackmviadvanced.fragments.main.create_blog.CreateBlogFragmentFactory
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
object MainFragmentsModule {

    @JvmStatic
    @MainScope
    @Provides
    @Named("AccountFragmentFactory")
    fun provideAccountFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory
    ): FragmentFactory {
        return AccountFragmentFactory(viewModelFactory)
    }

    @JvmStatic
    @MainScope
    @Provides
    @Named("BlogFragmentFactory")
    fun provideBlogFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory,
        requestManager: RequestManager
    ): FragmentFactory {
        return BlogFragmentFactory(viewModelFactory, requestManager)
    }

    @JvmStatic
    @MainScope
    @Provides
    @Named("CreateBlogFragmentFactory")
    fun provideCreateBlogFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory,
        requestManager: RequestManager
    ): FragmentFactory {
        return CreateBlogFragmentFactory(viewModelFactory, requestManager)
    }

}