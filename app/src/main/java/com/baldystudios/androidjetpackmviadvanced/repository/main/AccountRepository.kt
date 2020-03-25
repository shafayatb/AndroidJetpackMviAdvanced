package com.baldystudios.androidjetpackmviadvanced.repository.main

import com.baldystudios.androidjetpackmviadvanced.di.main.MainScope
import com.baldystudios.androidjetpackmviadvanced.models.AccountProperties
import com.baldystudios.androidjetpackmviadvanced.models.AuthToken
import com.baldystudios.androidjetpackmviadvanced.ui.main.account.state.AccountViewState
import com.baldystudios.androidjetpackmviadvanced.util.DataState
import com.baldystudios.androidjetpackmviadvanced.util.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@MainScope
interface AccountRepository {

    fun getAccountProperties(
        authToken: AuthToken,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun saveAccountProperties(
        authToken: AuthToken,
        email: String,
        username:String,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun updatePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>
}