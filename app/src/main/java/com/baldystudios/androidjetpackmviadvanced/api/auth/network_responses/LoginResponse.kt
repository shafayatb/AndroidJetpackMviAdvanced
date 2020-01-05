package com.baldystudios.androidjetpackmviadvanced.api.auth.network_responses

import com.squareup.moshi.Json

class LoginResponse(

    @Json(name = "response")
    var response: String,

    @Json(name = "error_message")
    var errorMessage: String,

    @Json(name = "token")
    var token: String,

    @Json(name = "pk")
    var pk: Int,

    @Json(name = "email")
    var email: String

) {
    override fun toString(): String {
        return "LoginResponse(response='$response', errorMessage='$errorMessage', token='$token', pk=$pk, email='$email')"
    }
}