package com.baldystudios.androidjetpackmviadvanced.di.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baldystudios.androidjetpackmviadvanced.di.main.keys.MainViewModelKey
import com.baldystudios.androidjetpackmviadvanced.ui.main.account.AccountViewModel
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.viewmodel.BlogViewModel
import com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog.CreateBlogViewModel
import com.baldystudios.androidjetpackmviadvanced.viewmodels.MainViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @MainScope
    @Binds
    abstract fun provideViewModelFactory(mainViewModelFactory: MainViewModelFactory): ViewModelProvider.Factory

    @MainScope
    @Binds
    @IntoMap
    @MainViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @MainScope
    @Binds
    @IntoMap
    @MainViewModelKey(BlogViewModel::class)
    abstract fun bindBlogViewModel(blogViewModel: BlogViewModel): ViewModel

    @MainScope
    @Binds
    @IntoMap
    @MainViewModelKey(CreateBlogViewModel::class)
    abstract fun bindCreateBlogViewModel(createBlogViewModel: CreateBlogViewModel): ViewModel

}