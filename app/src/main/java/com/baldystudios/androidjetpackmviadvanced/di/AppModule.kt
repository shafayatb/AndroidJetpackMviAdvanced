package com.baldystudios.androidjetpackmviadvanced.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.baldystudios.androidjetpackmviadvanced.R
import com.baldystudios.androidjetpackmviadvanced.persistence.AccountPropertiesDao
import com.baldystudios.androidjetpackmviadvanced.persistence.AppDatabase
import com.baldystudios.androidjetpackmviadvanced.persistence.AppDatabase.Companion.DATABASE_NAME
import com.baldystudios.androidjetpackmviadvanced.persistence.AuthTokenDao
import com.baldystudios.androidjetpackmviadvanced.util.Constants
import com.baldystudios.androidjetpackmviadvanced.util.LiveDataCallAdapterFactory
import com.baldystudios.androidjetpackmviadvanced.util.PreferenceKeys
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
object AppModule{

    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideAppDb(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration() // get correct db version if schema changed
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideAuthTokenDao(db: AppDatabase): AuthTokenDao {
        return db.getAuthTokenDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideAccountPropertiesDao(db: AppDatabase): AccountPropertiesDao {
        return db.getAccountPropertiesDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(R.drawable.default_image)
            .error(R.drawable.default_image)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGlideInstance(application: Application, requestOptions: RequestOptions): RequestManager {
        return Glide.with(application)
            .setDefaultRequestOptions(requestOptions)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application):SharedPreferences{
        return application.getSharedPreferences(PreferenceKeys.APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPrefsEditor(sharedPreferences: SharedPreferences):SharedPreferences.Editor{
        return sharedPreferences.edit()
    }

}