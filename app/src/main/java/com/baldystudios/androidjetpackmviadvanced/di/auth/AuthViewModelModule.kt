package com.baldystudios.androidjetpackmviadvanced.di.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baldystudios.androidjetpackmviadvanced.di.auth.keys.AuthViewModelKey
import com.baldystudios.androidjetpackmviadvanced.ui.auth.AuthViewModel
import com.baldystudios.androidjetpackmviadvanced.viewmodels.AuthViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class AuthViewModelModule {

    @AuthScope
    @Binds
    abstract fun bindViewModelFactory(authViewModelFactory: AuthViewModelFactory): ViewModelProvider.Factory

    @AuthScope
    @Binds
    @IntoMap
    @AuthViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel
}