package com.baldystudios.androidjetpackmviadvanced.ui.auth

import androidx.lifecycle.LiveData
import com.baldystudios.androidjetpackmviadvanced.models.AuthToken
import com.baldystudios.androidjetpackmviadvanced.repository.auth.AuthRepository
import com.baldystudios.androidjetpackmviadvanced.ui.BaseViewModel
import com.baldystudios.androidjetpackmviadvanced.ui.DataState
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.AuthStateEvent
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.AuthStateEvent.*
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.AuthViewState
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.LoginFields
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.RegistrationFields
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
) : BaseViewModel<AuthStateEvent, AuthViewState>() {


    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {

        return when (stateEvent) {

            is LoginAttemptEvent -> {
                authRepository.attemptLogin(
                    stateEvent.email,
                    stateEvent.password
                )
            }

            is RegisterAttemptEvent -> {

                authRepository.attemptRegistration(
                    stateEvent.email,
                    stateEvent.userName,
                    stateEvent.password,
                    stateEvent.confirm_password
                )
            }

            is CheckPreviousAuthEvent -> {
                authRepository.checkPreviousAuthUser()
            }
        }

    }

    fun setRegistrationFields(registrationFields: RegistrationFields) {
        val update = getCurrentViewStateOrNew()
        if (update.registrationFields == registrationFields) {
            return
        }
        update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun setLoginFields(loginFields: LoginFields) {
        val update = getCurrentViewStateOrNew()
        if (update.loginFields == loginFields) {
            return
        }
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()
        if (update.authToken == authToken) {
            return
        }
        update.authToken = authToken
        _viewState.value = update
    }

    fun cancelActiveJobs() {
        authRepository.cancelActiveJobs()
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}