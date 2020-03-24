package com.baldystudios.androidjetpackmviadvanced.repository.auth

import android.content.SharedPreferences
import com.baldystudios.androidjetpackmviadvanced.api.auth.OpenApiAuthService
import com.baldystudios.androidjetpackmviadvanced.api.auth.network_responses.LoginResponse
import com.baldystudios.androidjetpackmviadvanced.di.auth.AuthScope
import com.baldystudios.androidjetpackmviadvanced.models.AccountProperties
import com.baldystudios.androidjetpackmviadvanced.models.AuthToken
import com.baldystudios.androidjetpackmviadvanced.persistence.AccountPropertiesDao
import com.baldystudios.androidjetpackmviadvanced.persistence.AuthTokenDao
import com.baldystudios.androidjetpackmviadvanced.repository.emitError
import com.baldystudios.androidjetpackmviadvanced.repository.safeApiCall
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.AuthViewState
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.LoginFields
import com.baldystudios.androidjetpackmviadvanced.util.*
import com.baldystudios.androidjetpackmviadvanced.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import com.baldystudios.androidjetpackmviadvanced.util.ErrorHandling.Companion.INVALID_CREDENTIALS
import com.baldystudios.androidjetpackmviadvanced.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AuthScope
class AuthRepositoryImpl
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val sharedPrefsEditor: SharedPreferences.Editor
) : AuthRepository {

    private val TAG: String = "AppDebug"

    override fun attemptLogin(
        stateEvent: StateEvent,
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>> = flow {

        val loginFieldErrors = LoginFields(email, password).isValidForLogin()
        if(loginFieldErrors.equals(LoginFields.LoginError.none())){

            val apiResult = safeApiCall(IO){
                openApiAuthService.login(email, password)
            }
            emit(
                object: ApiResponseHandler<AuthViewState, LoginResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override fun handleSuccess(resultObj: LoginResponse): DataState<AuthViewState> {
                        // Incorrect login credentials counts as a 200 response from server, so need to handle that
                        if(resultObj.response.equals(GENERIC_AUTH_ERROR)){
                            return DataState.error(
                                response = Response(
                                    INVALID_CREDENTIALS,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }

                        CoroutineScope(IO).launch {

                            accountPropertiesDao.insertOrIgnore(
                                AccountProperties(
                                    resultObj.pk,
                                    resultObj.email,
                                    ""
                                )
                            )

                            // will return -1 if failure
                            val result = authTokenDao.insert(
                                AuthToken(
                                    resultObj.pk,
                                    resultObj.token
                                )
                            )
                            if(result < 0){
                                return DataState.error(
                                    response = Response(
                                        INVALID_CREDENTIALS,
                                        UIComponentType.Dialog(),
                                        MessageType.Error()
                                    ),
                                    stateEvent = stateEvent
                                )
                            }
                        }


                    }

                }.result
            )
        }
        else{
            emitError<AuthViewState>(
                loginFieldErrors,
                UIComponentType.Dialog(),
                stateEvent
            )
        }
    }

    override fun attemptRegistration(
        stateEvent: StateEvent,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>> = flow {

    }


    override fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>> = flow {

    }

    override fun saveAuthenticatedUserToPrefs(email: String) = flow {

    }

    override fun returnNoTokenFound(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>> = flow {

        emitError<AuthViewState>(
            RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
            UIComponentType.Dialog(),
            stateEvent
        )
    }

}