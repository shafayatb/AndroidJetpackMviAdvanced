package com.baldystudios.androidjetpackmviadvanced.di

import android.app.Application
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthComponent
import com.baldystudios.androidjetpackmviadvanced.di.main.MainComponent
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import com.baldystudios.androidjetpackmviadvanced.ui.BaseActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SubComponentsModule::class
    ]
)
interface AppComponent {

    val sessionManager: SessionManager

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(baseActivity: BaseActivity)

    fun authComponent(): AuthComponent.Factory

    fun mainComponent(): MainComponent.Factory
}