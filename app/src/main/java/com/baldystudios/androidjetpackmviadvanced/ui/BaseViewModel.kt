package com.baldystudios.androidjetpackmviadvanced.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baldystudios.androidjetpackmviadvanced.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<ViewState> : ViewModel() {
    val TAG: String = "AppDebug"

    protected val dataChannel = ConflatedBroadcastChannel<DataState<ViewState>>()

    protected val _viewState: MutableLiveData<ViewState> = MutableLiveData()
    protected val _activeJobCounter: MutableLiveData<HashSet<StateEvent>> = MutableLiveData()
    val messageStack = MessageStack()

    val viewState: LiveData<ViewState>
        get() = _viewState

    val activeJobCounter: LiveData<HashSet<StateEvent>>
        get() = _activeJobCounter

    val stateMessage: LiveData<StateMessage>
            = messageStack.stateMessage

    init {
        setupChannel()
    }

    private fun setupChannel(){
        dataChannel
            .asFlow()
            .onEach{ dataState ->
                dataState.data?.let { data ->
                    handleNewData(dataState.stateEvent, data)
                }
                dataState.stateMessage?.let { error ->
                    handleNewError(dataState.stateEvent, error)
                }
            }
            .launchIn(viewModelScope)
    }

    abstract fun handleNewData(stateEvent: StateEvent?, data: ViewState)

    abstract fun setStateEvent(stateEvent: StateEvent)

    fun handleNewError(stateEvent: StateEvent?, stateMessage: StateMessage){
        appendStateMessage(stateMessage)
        removeJobFromCounter(stateEvent)
    }

    fun launchJob(
        stateEvent: StateEvent,
        jobFunction: Flow<DataState<ViewState>>
    ){
        if(!isJobAlreadyActive(stateEvent)){
            addJobToCounter(stateEvent)
            jobFunction
                .onEach { dataState ->
                    offerToDataChannel(dataState)
                }
                .launchIn(viewModelScope)
        }
    }

    fun clearActiveJobCounter(){
        _activeJobCounter.value?.clear()
    }

    fun addJobToCounter(stateEvent: StateEvent){
        _activeJobCounter.value?.add(stateEvent)
    }

    fun removeJobFromCounter(stateEvent: StateEvent?){
        _activeJobCounter.value?.remove(stateEvent)
    }

    fun areAnyJobsActive(): Boolean{
        return _activeJobCounter.value?.let{
            it.size > 0
        }?: false
    }

    fun getNumActiveJobs(): Int {
        return _activeJobCounter.value?.size ?: 0
    }

    fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return _activeJobCounter.value?.contains(stateEvent) ?: false
    }

    private fun offerToDataChannel(dataState: DataState<ViewState>){
        if(!dataChannel.isClosedForSend){
            dataChannel.offer(dataState)
        }
    }

    fun getCurrentViewStateOrNew(): ViewState{
        val value = viewState.value?.let{
            it
        }?: initNewViewState()
        return value
    }

    fun setViewState(viewState: ViewState){
        _viewState.value = viewState
    }

    private fun appendStateMessage(stateMessage: StateMessage) {
        messageStack.add(stateMessage)
    }

    fun clearStateMessage(index: Int = 0){
        messageStack.removeAt(index)
    }

    abstract fun initNewViewState(): ViewState

}