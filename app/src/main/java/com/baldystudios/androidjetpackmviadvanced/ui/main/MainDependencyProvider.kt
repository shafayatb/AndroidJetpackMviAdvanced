package com.baldystudios.androidjetpackmviadvanced.ui.main

import com.baldystudios.androidjetpackmviadvanced.viewmodels.AuthViewModelFactory
import com.bumptech.glide.RequestManager

interface MainDependencyProvider {

    fun getViewModelProviderFactory(): AuthViewModelFactory

    fun getGlideRequestManager(): RequestManager

}