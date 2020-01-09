package com.baldystudios.androidjetpackmviadvanced.di.auth

import com.baldystudios.androidjetpackmviadvanced.ui.auth.ForgotPasswordFragment
import com.baldystudios.androidjetpackmviadvanced.ui.auth.LauncherFragment
import com.baldystudios.androidjetpackmviadvanced.ui.auth.LoginFragment
import com.baldystudios.androidjetpackmviadvanced.ui.auth.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeLauncherFragment(): LauncherFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment

}