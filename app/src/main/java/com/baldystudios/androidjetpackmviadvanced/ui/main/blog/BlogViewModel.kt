package com.baldystudios.androidjetpackmviadvanced.ui.main.blog

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.baldystudios.androidjetpackmviadvanced.models.BlogPost
import com.baldystudios.androidjetpackmviadvanced.repository.main.BlogRepository
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import com.baldystudios.androidjetpackmviadvanced.ui.BaseViewModel
import com.baldystudios.androidjetpackmviadvanced.ui.DataState
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogStateEvent
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogStateEvent.BlogSearchEvent
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogStateEvent.None
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogViewState
import com.baldystudios.androidjetpackmviadvanced.util.AbsentLiveData
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class BlogViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val blogRepository: BlogRepository,
    private val sharedPreferences: SharedPreferences,
    private val requestManager: RequestManager
) : BaseViewModel<BlogStateEvent, BlogViewState>() {


    override fun initNewViewState(): BlogViewState {
        return BlogViewState()
    }

    override fun handleStateEvent(stateEvent: BlogStateEvent): LiveData<DataState<BlogViewState>> {
        return when (stateEvent) {

            is BlogSearchEvent -> {
                AbsentLiveData.create()
            }
            is None -> {
                AbsentLiveData.create()
            }
        }
    }

    fun setQuery(query: String) {
        val update = getCurrentViewStateOrNew()
        if (query == update.blogFields.searchQuery) {
            return
        }
        update.blogFields.searchQuery = query
        _viewState.value = update
    }

    fun setBlogListData(blogPost: List<BlogPost>) {
        val update = getCurrentViewStateOrNew()
        update.blogFields.blogList = blogPost
        _viewState.value = update
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