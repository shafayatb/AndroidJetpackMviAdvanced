package com.baldystudios.androidjetpackmviadvanced

import android.app.Application
import com.baldystudios.androidjetpackmviadvanced.di.AppComponent
import com.baldystudios.androidjetpackmviadvanced.di.DaggerAppComponent
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthComponent


class BaseApplication : Application() {

    lateinit var appComponent: AppComponent

    private var authComponent: AuthComponent? = null

    override fun onCreate() {
        super.onCreate()
    }

    fun authComponent(): AuthComponent{
        if(authComponent == null){
            authComponent = appComponent.authComponent().create()
        }
        return (authComponent as AuthComponent)
    }

    fun releaseAuthComponent(){
        authComponent = null
    }

    fun initAppComponent(){
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }

}