package com.baldystudios.androidjetpackmviadvanced.api.auth.network_responses

import com.squareup.moshi.Json

class RegistrationResponse(

    @Json(name = "response")
    var response: String,

    @Json(name = "error_message")
    var errorMessage: String,

    @Json(name = "email")
    var email: String,

    @Json(name = "username")
    var username: String,

    @Json(name = "pk")
    var pk: Int,

    @Json(name = "token")
    var token: String)
{

    override fun toString(): String {
        return "RegistrationResponse(response='$response', errorMessage='$errorMessage', email='$email', username='$username', token='$token')"
    }
}