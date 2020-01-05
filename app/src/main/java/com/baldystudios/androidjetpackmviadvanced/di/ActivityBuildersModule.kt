package com.baldystudios.androidjetpackmviadvanced.di

import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthFragmentBuildersModule
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthModule
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthScope
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthViewModelModule
import com.baldystudios.androidjetpackmviadvanced.ui.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

}