package com.baldystudios.androidjetpackmviadvanced.di.auth

import androidx.lifecycle.ViewModel
import com.baldystudios.androidjetpackmviadvanced.di.ViewModelKey
import com.baldystudios.androidjetpackmviadvanced.ui.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class AuthViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel
}