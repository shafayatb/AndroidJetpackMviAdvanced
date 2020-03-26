package com.baldystudios.androidjetpackmviadvanced.ui.main.blog.viewmodel

import android.util.Log
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogStateEvent.BlogSearchEvent
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogStateEvent.RestoreBlogListFromCache
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.BlogViewState

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.refreshFromCache(){
    setQueryExhausted(false)
    setStateEvent(RestoreBlogListFromCache())
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.loadFirstPage() {
    setQueryExhausted(false)
    resetPage()
    setStateEvent(BlogSearchEvent())
    Log.e(TAG, "BlogViewModel: loadFirstPage: ${viewState.value!!.blogFields.searchQuery}")
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
private fun BlogViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.copy().blogFields.page // get current page
    update.blogFields.page = page?.plus(1)
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.nextPage(){
    if(!isJobAlreadyActive(BlogSearchEvent())
        && !viewState.value!!.blogFields.isQueryExhausted!!
    ){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setStateEvent(BlogSearchEvent())
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.handleIncomingBlogListData(viewState: BlogViewState){
    Log.d(TAG, "BlogViewModel, DataState: ${viewState}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryExhausted?: " +
            "${viewState.blogFields.isQueryExhausted}")
    viewState.blogFields.let { blogFields ->
        blogFields.blogList?.let { setBlogListData(it) }
        blogFields.isQueryExhausted?.let {  setQueryExhausted(it) }
    }
}