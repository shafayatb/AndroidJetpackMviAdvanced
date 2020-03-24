package com.baldystudios.androidjetpackmviadvanced.ui.main.blog.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.baldystudios.androidjetpackmviadvanced.persistence.BlogQueryUtils
import com.baldystudios.androidjetpackmviadvanced.repository.main.BlogRepositoryImpl
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import com.baldystudios.androidjetpackmviadvanced.ui.BaseViewModel
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogStateEvent
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogStateEvent.*
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogViewState
import com.baldystudios.androidjetpackmviadvanced.util.AbsentLiveData
import com.baldystudios.androidjetpackmviadvanced.util.DataState
import com.baldystudios.androidjetpackmviadvanced.util.PreferenceKeys.Companion.BLOG_FILTER
import com.baldystudios.androidjetpackmviadvanced.util.PreferenceKeys.Companion.BLOG_ORDER
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

class BlogViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val blogRepository: BlogRepositoryImpl,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) : BaseViewModel<BlogStateEvent, BlogViewState>() {

    init {
        setBlogFilter(
            sharedPreferences.getString(
                BLOG_FILTER,
                BlogQueryUtils.BLOG_FILTER_DATE_UPDATED
            )
        )

        sharedPreferences.getString(
            BLOG_ORDER,
            BlogQueryUtils.BLOG_ORDER_ASC
        )?.let {
            setBlogOrder(
                it
            )
        }
    }

    override fun initNewViewState(): BlogViewState {
        return BlogViewState()
    }

    override fun handleStateEvent(stateEvent: BlogStateEvent): LiveData<DataState<BlogViewState>> {
        return when (stateEvent) {

            is BlogSearchEvent -> {
                clearLayoutManagerState()
                sessionManager.cachedToken.value?.let { authToken ->
                    blogRepository.searchBlogPost(
                        authToken,
                        getSearchQuery(),
                        getOrder() + getFilter(),
                        getPage()
                    )
                } ?: AbsentLiveData.create()
            }

            is RestoreBlogListFromCache -> {
                return blogRepository.restoreBlogListFromCache(
                    query = getSearchQuery(),
                    filerAndOrder = getOrder() + getFilter(),
                    page = getPage()
                )
            }

            is CheckAuthorOfBlogPost -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    blogRepository.isAuthorOfBlogPost(
                        authToken,
                        getSlug()
                    )
                } ?: AbsentLiveData.create()
            }

            is DeleteBlogPostEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    blogRepository.deleteBlogPost(
                        authToken,
                        getBlogPost()
                    )
                } ?: AbsentLiveData.create()
            }

            is UpdatedBlogPostEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    val title = RequestBody.create(
                        MediaType.parse("text/plain"),
                        stateEvent.title
                    )
                    val body = RequestBody.create(
                        MediaType.parse("text/plain"),
                        stateEvent.body
                    )

                    blogRepository.updateBlogPost(
                        authToken,
                        getSlug(),
                        title,
                        body,
                        stateEvent.image
                    )

                } ?: AbsentLiveData.create()
            }

            is None -> {
                object : LiveData<DataState<BlogViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        value =
                            DataState(
                                null,
                                Loading(false),
                                null
                            )
                    }
                }
            }

        }
    }


    fun saveFilterOptions(filter: String, order: String) {
        editor.putString(BLOG_FILTER, filter)
        editor.apply()

        editor.putString(BLOG_ORDER, order)
        editor.apply()
    }

    fun cancelActiveJobs() {
        blogRepository.cancelActiveJobs()
        handlePendingData()

    }

    private fun handlePendingData() {
        setStateEvent(None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }


}