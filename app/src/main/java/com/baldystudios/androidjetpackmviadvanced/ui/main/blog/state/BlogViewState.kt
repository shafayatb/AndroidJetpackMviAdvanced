package com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state

import com.baldystudios.androidjetpackmviadvanced.models.BlogPost
import com.baldystudios.androidjetpackmviadvanced.persistence.BlogQueryUtils.Companion.BLOG_ORDER_ASC
import com.baldystudios.androidjetpackmviadvanced.persistence.BlogQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED

data class BlogViewState(
    // BLogFragment vars
    var blogFields: BlogFields = BlogFields(),
    // ViewBlogFragment vars
    var viewBlogFields: ViewBlogFields = ViewBlogFields()
    // UpdateBlogFragment vars
) {
    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList(),
        var searchQuery: String = "",
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false,
        var filter: String = ORDER_BY_ASC_DATE_UPDATED,
        var order: String = BLOG_ORDER_ASC
    )

    data class ViewBlogFields(
        var blogPost: BlogPost? = null,
        var isAuthorOfBlogPost: Boolean = false
    )
}