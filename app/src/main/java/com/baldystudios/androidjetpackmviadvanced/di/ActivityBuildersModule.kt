package com.baldystudios.androidjetpackmviadvanced.di

import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthFragmentBuildersModule
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthModule
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthScope
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthViewModelModule
import com.baldystudios.androidjetpackmviadvanced.di.main.MainFragmentBuildersModule
import com.baldystudios.androidjetpackmviadvanced.di.main.MainModule
import com.baldystudios.androidjetpackmviadvanced.di.main.MainScope
import com.baldystudios.androidjetpackmviadvanced.di.main.MainViewModelModule
import com.baldystudios.androidjetpackmviadvanced.ui.auth.AuthActivity
import com.baldystudios.androidjetpackmviadvanced.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

    @MainScope
    @ContributesAndroidInjector(
        modules = [MainModule::class, MainFragmentBuildersModule::class, MainViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity

}