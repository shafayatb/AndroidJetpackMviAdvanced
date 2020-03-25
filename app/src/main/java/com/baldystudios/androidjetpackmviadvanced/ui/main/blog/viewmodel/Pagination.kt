package com.baldystudios.androidjetpackmviadvanced.ui.main.blog.viewmodel

import android.util.Log
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogStateEvent.BlogSearchEvent
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogStateEvent.RestoreBlogListFromCache
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogViewState

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.resetPage() {
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.refreshFromCache() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    setStateEvent(RestoreBlogListFromCache())
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(BlogSearchEvent())
    Log.e(TAG, "BlogViewModel: loadFirstPage: ${viewState.value!!.blogFields.searchQuery}")
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
private fun BlogViewModel.incrementPageNumber() {
    val update = getCurrentViewStateOrNew()
    val page = update.copy().blogFields.page // get current page
    update.blogFields.page = page + 1
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.nextPage() {
    if (!viewState.value!!.blogFields.isQueryInProgress
        && !viewState.value!!.blogFields.isQueryExhausted
    ) {
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(BlogSearchEvent())
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.handleIncomingBlogListData(viewState: BlogViewState) {
    Log.d(TAG, "BlogViewModel, DataState: ${viewState}")
    Log.d(
        TAG, "BlogViewModel, DataState: isQueryInProgress?: " +
                "${viewState.blogFields.isQueryInProgress}"
    )
    Log.d(
        TAG, "BlogViewModel, DataState: isQueryExhausted?: " +
                "${viewState.blogFields.isQueryExhausted}"
    )
    setQueryInProgress(viewState.blogFields.isQueryInProgress)
    setQueryExhausted(viewState.blogFields.isQueryExhausted)
    setBlogListData(viewState.blogFields.blogList)
}