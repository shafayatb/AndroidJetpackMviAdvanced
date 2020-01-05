package com.baldystudios.androidjetpackmviadvanced.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.baldystudios.androidjetpackmviadvanced.api.auth.network_responses.LoginResponse
import com.baldystudios.androidjetpackmviadvanced.api.auth.network_responses.RegistrationResponse
import com.baldystudios.androidjetpackmviadvanced.repository.auth.AuthRepository
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
) : ViewModel() {

    fun testLogin(): LiveData<GenericApiResponse<LoginResponse>> {
        return authRepository.testLoginRequest(
            "",
            ""
        )
    }

    fun testRegistration(): LiveData<GenericApiResponse<RegistrationResponse>> {
        val userMap = LinkedHashMap<String, String>()
        userMap["email"] = ""
        userMap["username"] = ""
        userMap["password"] = ""
        userMap["password2"] = ""

        return authRepository.testRegistrationRequest(userMap)
    }

}