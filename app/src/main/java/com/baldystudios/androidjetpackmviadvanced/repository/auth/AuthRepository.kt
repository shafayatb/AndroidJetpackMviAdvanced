package com.baldystudios.androidjetpackmviadvanced.repository.auth


import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthScope
import com.baldystudios.androidjetpackmviadvanced.util.DataState
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.AuthViewState
import com.baldystudios.androidjetpackmviadvanced.util.StateEvent
import kotlinx.coroutines.flow.Flow

@AuthScope
interface AuthRepository {

    fun attemptLogin(
        stateEvent: StateEvent,
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>>

    fun attemptRegistration(
        stateEvent: StateEvent,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>>

    fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>>

    fun saveAuthenticatedUserToPrefs(email: String)

    fun returnNoTokenFound(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>>

}