package com.baldystudios.androidjetpackmviadvanced.di.main

import androidx.lifecycle.ViewModel
import com.baldystudios.androidjetpackmviadvanced.di.ViewModelKey
import com.baldystudios.androidjetpackmviadvanced.ui.main.account.AccountViewModel
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.BlogViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BlogViewModel::class)
    abstract fun bindBlogViewModel(blogViewModel: BlogViewModel): ViewModel

}