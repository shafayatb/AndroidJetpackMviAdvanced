package com.baldystudios.androidjetpackmviadvanced.api.auth.network_responses

import com.squareup.moshi.Json

class LoginResponse(

    @Json(name = "response")
    var response: String,

    @Json(name = "error_message")
    var error_message: String,

    @Json(name = "token")
    var token: String,

    @Json(name = "pk")
    var pk: Int,

    @Json(name = "email")
    var email: String

) {
    override fun toString(): String {
        return "LoginResponse(response='$response', errorMessage='$error_message', token='$token', pk=$pk, email='$email')"
    }
}