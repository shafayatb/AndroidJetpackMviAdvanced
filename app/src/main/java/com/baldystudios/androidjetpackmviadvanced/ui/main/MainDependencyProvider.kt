package com.baldystudios.androidjetpackmviadvanced.ui.main

import com.baldystudios.androidjetpackmviadvanced.viewmodels.ViewModelProviderFactory
import com.bumptech.glide.RequestManager

interface MainDependencyProvider {

    fun getViewModelProviderFactory(): ViewModelProviderFactory

    fun getGlideRequestManager(): RequestManager

}