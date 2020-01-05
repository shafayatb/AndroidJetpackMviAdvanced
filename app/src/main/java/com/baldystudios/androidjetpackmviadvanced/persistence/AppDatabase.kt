package com.baldystudios.androidjetpackmviadvanced.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baldystudios.androidjetpackmviadvanced.models.AccountProperties
import com.baldystudios.androidjetpackmviadvanced.models.AuthToken


@Database(entities = [AuthToken::class, AccountProperties::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    companion object {

        const val DATABASE_NAME = "app_db"

    }

}