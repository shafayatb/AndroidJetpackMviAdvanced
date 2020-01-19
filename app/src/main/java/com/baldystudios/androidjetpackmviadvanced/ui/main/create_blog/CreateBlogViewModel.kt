package com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.baldystudios.androidjetpackmviadvanced.repository.main.CreateBlogRepository
import com.baldystudios.androidjetpackmviadvanced.session.SessionManager
import com.baldystudios.androidjetpackmviadvanced.ui.BaseViewModel
import com.baldystudios.androidjetpackmviadvanced.ui.DataState
import com.baldystudios.androidjetpackmviadvanced.ui.Loading
import com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog.state.CreateBlogStateEvent
import com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog.state.CreateBlogStateEvent.CreateNewBlogEvent
import com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog.state.CreateBlogStateEvent.None
import com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog.state.CreateBlogViewState
import com.baldystudios.androidjetpackmviadvanced.ui.main.create_blog.state.CreateBlogViewState.NewBlogFields
import com.baldystudios.androidjetpackmviadvanced.util.AbsentLiveData
import javax.inject.Inject

class CreateBlogViewModel
@Inject
constructor(
    val createBlogRepository: CreateBlogRepository,
    val sessionManager: SessionManager
) : BaseViewModel<CreateBlogStateEvent, CreateBlogViewState>() {

    override fun initNewViewState(): CreateBlogViewState {
        return CreateBlogViewState()
    }

    override fun handleStateEvent(stateEvent: CreateBlogStateEvent): LiveData<DataState<CreateBlogViewState>> {
        return when (stateEvent) {
            is CreateNewBlogEvent -> {
                AbsentLiveData.create()
            }
            is None -> {
                liveData {
                    emit(
                        DataState(
                            null,
                            Loading(false),
                            null
                        )
                    )
                }
            }
        }
    }

    fun setNewBlogFields(title: String?, body: String?, uri: Uri?) {
        val update = getCurrentViewStateOrNew()
        val newBlogFields = update.blogFields
        title?.let { newBlogFields.newBlogTitle = it }
        body?.let { newBlogFields.newBlogBody = it }
        uri?.let { newBlogFields.newImageUri = it }
        update.blogFields = newBlogFields
        setViewState(update)
    }

    fun clearNewBlogFields() {
        val update = getCurrentViewStateOrNew()
        update.blogFields = NewBlogFields()
        setViewState(update)
    }

    fun cancelActiveJobs() {
        createBlogRepository.cancelActiveJobs()
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