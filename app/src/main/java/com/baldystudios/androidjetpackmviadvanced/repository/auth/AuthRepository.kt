package com.baldystudios.androidjetpackmviadvanced.repository.auth


import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.baldystudios.androidjetpackmviadvanced.api.auth.OpenApiAuthService
import com.baldystudios.androidjetpackmviadvanced.api.auth.network_responses.LoginResponse
import com.baldystudios.androidjetpackmviadvanced.api.auth.network_responses.RegistrationResponse
import com.baldystudios.androidjetpackmviadvanced.models.AccountProperties
import com.baldystudios.androidjetpackmviadvanced.models.AuthToken
import com.baldystudios.androidjetpackmviadvanced.persistence.AccountPropertiesDao
import com.baldystudios.androidjetpackmviadvanced.persistence.AuthTokenDao
import com.baldystudios.androidjetpackmviadvanced.repository.NetworkBoundResource
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import com.baldystudios.androidjetpackmviadvanced.ui.DataState
import com.baldystudios.androidjetpackmviadvanced.ui.Response
import com.baldystudios.androidjetpackmviadvanced.ui.ResponseType
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.AuthViewState
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.LoginFields
import com.baldystudios.androidjetpackmviadvanced.ui.auth.state.RegistrationFields
import com.baldystudios.androidjetpackmviadvanced.util.AbsentLiveData
import com.baldystudios.androidjetpackmviadvanced.util.ErrorHandling.Companion.ERROR_SAVE_AUTH_TOKEN
import com.baldystudios.androidjetpackmviadvanced.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse.ApiSuccessResponse
import com.baldystudios.androidjetpackmviadvanced.util.PreferenceKeys
import com.baldystudios.androidjetpackmviadvanced.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import kotlinx.coroutines.Job
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val sharedPrefsEditor: SharedPreferences.Editor
) {
    private val TAG: String = "AppDebug"

    private var repositoryJob: Job? = null

    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>> {

        val loginFieldErrors = LoginFields(email, password).isValidForLogin()

        if (loginFieldErrors != LoginFields.LoginError.none()) {
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())
        }

        return object : NetworkBoundResource<LoginResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true
        ) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: $response")

                if (response.body.response == GENERIC_AUTH_ERROR) {
                    onErrorReturn(response.body.error_message, true, false)
                }

                accountPropertiesDao.insertOrIgnore(
                    AccountProperties(
                        response.body.pk,
                        response.body.email,
                        ""
                    )
                )

                val result = authTokenDao.insert(
                    AuthToken(
                        response.body.pk,
                        response.body.token
                    )
                )

                if (result < 0) {
                    return onCompleteJob(
                        DataState.error(
                            Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                        )
                    )
                }

                saveAuthenticatedUserToPrefs(email)

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.pk, response.body.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return openApiAuthService.login(email, password)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

            //Ignore
            override suspend fun createCacheRequestAndReturn() {

            }

        }.asLiveData()

    }

    fun attemptRegistration(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>> {

        val registrationFieldErrors =
            RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()

        if (registrationFieldErrors != RegistrationFields.RegistrationError.none()) {
            return returnErrorResponse(registrationFieldErrors, ResponseType.Dialog())
        }

        val userMap = LinkedHashMap<String, String>()
        userMap["email"] = email
        userMap["username"] = username
        userMap["password"] = password
        userMap["password2"] = confirmPassword

        return object : NetworkBoundResource<RegistrationResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true
        ) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RegistrationResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: $response")

                if (response.body.response == GENERIC_AUTH_ERROR) {
                    onErrorReturn(response.body.errorMessage, true, false)
                }

                accountPropertiesDao.insertOrIgnore(
                    AccountProperties(
                        response.body.pk,
                        response.body.email,
                        ""
                    )
                )

                val result = authTokenDao.insert(
                    AuthToken(
                        response.body.pk,
                        response.body.token
                    )
                )

                if (result < 0) {
                    return onCompleteJob(
                        DataState.error(
                            Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                        )
                    )
                }

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.pk, response.body.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<RegistrationResponse>> {
                return openApiAuthService.register(userMap)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

            //Ignore
            override suspend fun createCacheRequestAndReturn() {

            }

        }.asLiveData()

    }

    fun checkPreviousAuthUser(): LiveData<DataState<AuthViewState>> {

        val previousAuthUserEmail: String? =
            sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)

        if (previousAuthUserEmail.isNullOrBlank()) {
            Log.d(TAG, "checkPreviousAuthUser: No previous authenticated user found")
            return returnNoTokenFound()
        }

        return object : NetworkBoundResource<Void, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            false
        ) {
            override suspend fun createCacheRequestAndReturn() {

                accountPropertiesDao.searchByEmail(previousAuthUserEmail).let { accountProperties ->

                    Log.d(TAG, "checkPreviousAuthUser: searching for token $accountProperties")

                    accountProperties?.let {
                        if (accountProperties.pk > -1) {
                            authTokenDao.searchByPk(accountProperties.pk).let { authToken ->

                                if (authToken != null) {
                                    onCompleteJob(
                                        DataState.data(
                                            data = AuthViewState(
                                                authToken = authToken
                                            )
                                        )
                                    )
                                    return
                                }

                            }
                        }
                    }
                    Log.d(TAG, "checkPreviousAuthUser: AuthToken not found...")

                    onCompleteJob(
                        DataState.data(
                            data = null,
                            response = Response(
                                RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                                ResponseType.None()
                            )
                        )
                    )

                }

            }

            //Ignore
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<Void>) {

            }

            // Ignore
            override fun createCall(): LiveData<GenericApiResponse<Void>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }
        }.asLiveData()

    }

    private fun returnNoTokenFound(): LiveData<DataState<AuthViewState>> {
        return object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.data(
                    data = null,
                    response = Response(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE, ResponseType.None())
                )
            }
        }
    }

    private fun saveAuthenticatedUserToPrefs(email: String) {
        sharedPrefsEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, email)
        sharedPrefsEditor.apply()
    }

    private fun returnErrorResponse(
        errorMessage: String,
        responseType: ResponseType
    ): LiveData<DataState<AuthViewState>> {
        Log.d(TAG, "returnErrorResponse: $errorMessage")
        return object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(
                        errorMessage,
                        responseType
                    )
                )
            }
        }
    }

    fun cancelActiveJobs() {
        Log.d(TAG, "AuthRepository: Cancelling on-going jobs...")
        repositoryJob?.cancel()
    }

}