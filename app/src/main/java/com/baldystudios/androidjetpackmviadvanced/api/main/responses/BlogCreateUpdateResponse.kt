package com.baldystudios.androidjetpackmviadvanced.api.main.responses

import com.baldystudios.androidjetpackmviadvanced.models.BlogPost
import com.baldystudios.androidjetpackmviadvanced.util.DateUtils
import com.squareup.moshi.Json

class BlogCreateUpdateResponse(

    @Json(name = "response")
    var response: String,

    @Json(name = "pk")
    var pk: Int,

    @Json(name = "title")
    var title: String,

    @Json(name = "slug")
    var slug: String,

    @Json(name = "body")
    var body: String,

    @Json(name = "image")
    var image: String,

    @Json(name = "date_updated")
    var date_updated: String,

    @Json(name = "username")
    var username: String

) {
    fun toBlogPost(): BlogPost {
        return BlogPost(
            pk = pk,
            title = title,
            slug = slug,
            body = body,
            image = image,
            date_updated = DateUtils.convertServerStringDateToLong(
                date_updated
            ),
            username = username
        )
    }
}
