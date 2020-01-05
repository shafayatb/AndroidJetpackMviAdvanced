package com.baldystudios.androidjetpackmviadvanced.ui.auth

import androidx.lifecycle.ViewModel
import com.baldystudios.androidjetpackmviadvanced.repository.auth.AuthRepository
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
): ViewModel(){



}