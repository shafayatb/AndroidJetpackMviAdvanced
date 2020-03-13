package com.baldystudios.androidjetpackmviadvanced.di

import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthComponent
import com.baldystudios.androidjetpackmviadvanced.di.main.MainComponent
import dagger.Module

@Module(
    subcomponents = [
        AuthComponent::class,
        MainComponent::class
    ]
)
class SubComponentsModule