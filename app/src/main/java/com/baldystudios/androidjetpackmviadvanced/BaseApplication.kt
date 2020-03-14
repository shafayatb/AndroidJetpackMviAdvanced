package com.baldystudios.androidjetpackmviadvanced

import android.app.Application
import com.baldystudios.androidjetpackmviadvanced.di.AppComponent
import com.baldystudios.androidjetpackmviadvanced.di.DaggerAppComponent
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthComponent
import com.baldystudios.androidjetpackmviadvanced.di.main.MainComponent


class BaseApplication : Application() {

    lateinit var appComponent: AppComponent

    private var authComponent: AuthComponent? = null

    private var mainComponent: MainComponent? = null

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
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

    fun mainComponent(): MainComponent{
        if(mainComponent == null){
            mainComponent = appComponent.mainComponent().create()
        }
        return (mainComponent as MainComponent)
    }

    fun releaseMainComponent(){
        mainComponent = null
    }

    fun initAppComponent(){
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }

}