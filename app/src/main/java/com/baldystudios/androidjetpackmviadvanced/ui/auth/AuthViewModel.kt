package com.baldystudios.androidjetpackmviadvanced.ui.auth

import androidx.lifecycle.ViewModel
import com.baldystudios.androidjetpackmviadvanced.repository.auth.AuthRepository

class AuthViewModel
constructor(
    val authRepository: AuthRepository
): ViewModel(){



}