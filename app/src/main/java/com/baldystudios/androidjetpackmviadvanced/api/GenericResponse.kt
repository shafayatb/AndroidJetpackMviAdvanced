package com.baldystudios.androidjetpackmviadvanced.api

import com.squareup.moshi.Json

class GenericResponse (
    @Json(name = "response")
    var response: String?
)