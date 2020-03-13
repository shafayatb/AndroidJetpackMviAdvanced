package com.baldystudios.androidjetpackmviadvanced.di.auth

import com.baldystudios.androidjetpackmviadvanced.ui.auth.AuthActivity
import dagger.Subcomponent

@Subcomponent(
    modules = [
        AuthModule::class,
        AuthViewModelModule::class,
        AuthFragmentsModule::class
    ]
)
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(): AuthComponent

    }

    fun inject(authActivity: AuthActivity)

}