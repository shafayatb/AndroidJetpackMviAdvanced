package com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state

import com.baldystudios.androidjetpackmviadvanced.models.BlogPost

data class BlogViewState(
    // BLogFragment vars
    var blogFields: BlogFields = BlogFields()
    // ViewBlogFragment vars

    // UpdateBlogFragment vars
){
    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList(),
        var searchQuery: String = ""
    )
}