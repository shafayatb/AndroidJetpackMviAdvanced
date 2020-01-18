package com.baldystudios.androidjetpackmviadvanced.api.main.responses

import com.squareup.moshi.Json

class BlogCreateUpdateResponse(

    @Json(name = "response")
    var response: String,

    @Json(name ="pk")
    var pk: Int,

    @Json(name ="title")
    var title: String,

    @Json(name ="slug")
    var slug: String,

    @Json(name ="body")
    var body: String,

    @Json(name ="image")
    var image: String,

    @Json(name ="date_updated")
    var date_updated: String,

    @Json(name ="username")
    var username: String



)