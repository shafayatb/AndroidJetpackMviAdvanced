package com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state

import android.net.Uri
import android.os.Parcelable
import com.baldystudios.androidjetpackmviadvanced.models.BlogPost
import com.baldystudios.androidjetpackmviadvanced.persistence.BlogQueryUtils.Companion.BLOG_ORDER_ASC
import com.baldystudios.androidjetpackmviadvanced.persistence.BlogQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED
import kotlinx.android.parcel.Parcelize

const val BLOG_VIEW_STATE_BUNDLE_KEY = "com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogViewState"

@Parcelize
data class BlogViewState(
    // BLogFragment vars
    var blogFields: BlogFields = BlogFields(),
    // ViewBlogFragment vars
    var viewBlogFields: ViewBlogFields = ViewBlogFields(),
    // UpdateBlogFragment vars
    var updateBlogFields: UpdateBlogFields = UpdateBlogFields()
) : Parcelable {

    @Parcelize
    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList(),
        var searchQuery: String = "",
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false,
        var filter: String = ORDER_BY_ASC_DATE_UPDATED,
        var order: String = BLOG_ORDER_ASC,
        var layoutManagerState: Parcelable? = null
    ) : Parcelable

    @Parcelize
    data class ViewBlogFields(
        var blogPost: BlogPost? = null,
        var isAuthorOfBlogPost: Boolean = false
    ) : Parcelable

    @Parcelize
    data class UpdateBlogFields(
        var updatedBlogTitle: String? = null,
        var updatedBlogBody: String? = null,
        var updatedImageUri: Uri? = null
    ) : Parcelable
}