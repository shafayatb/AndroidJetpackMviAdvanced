package com.baldystudios.androidjetpackmviadvanced.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.baldystudios.androidjetpackmviadvanced.ui.DataState
import com.baldystudios.androidjetpackmviadvanced.ui.Response
import com.baldystudios.androidjetpackmviadvanced.ui.ResponseType
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.NETWORK_TIMEOUT
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.TESTING_CACHE_DELAY
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.TESTING_NETWORK_DELAY
import com.baldystudios.androidjetpackmviadvanced.util.ErrorHandling
import com.baldystudios.androidjetpackmviadvanced.util.ErrorHandling.Companion.ERROR_CHECK_NETWORK_CONNECTION
import com.baldystudios.androidjetpackmviadvanced.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.baldystudios.androidjetpackmviadvanced.util.ErrorHandling.Companion.UNABLE_TODO_OPERATION_WO_INTERNET
import com.baldystudios.androidjetpackmviadvanced.util.ErrorHandling.Companion.UNABLE_TO_RESOLVE_HOST
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


abstract class NetworkBoundResource<ResponseObject, ViewStateType>(
    isNetworkAvailable: Boolean,
    isNetworkRequest: Boolean
) {

    private val TAG: String = "AppDebug"

    protected val result = MediatorLiveData<DataState<ViewStateType>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    init {
        setJob(initNewJob())
        setValue(DataState.loading(isLoading = true, cachedData = null))

        if (isNetworkRequest) {
            if (isNetworkAvailable) {
                coroutineScope.launch {

                    delay(TESTING_NETWORK_DELAY)

                    withContext(Main) {
                        val apiResponse = createCall()
                        result.addSource(apiResponse) { response ->
                            result.removeSource(apiResponse)

                            coroutineScope.launch {
                                handleNetworkCall(response)
                            }
                        }
                    }

                }

                GlobalScope.launch(IO) {
                    delay(NETWORK_TIMEOUT)

                    if (!job.isCompleted) {
                        Log.e(TAG, "NetworkBoundResource: JOB NETWORK TIMEOUT")
                        job.cancel(CancellationException(UNABLE_TO_RESOLVE_HOST))
                    }
                }

            } else {
                onErrorReturn(
                    UNABLE_TODO_OPERATION_WO_INTERNET,
                    shouldUseDialog = true,
                    shouldeUseToast = false
                )
            }
        } else {
            coroutineScope.launch {
                delay(TESTING_CACHE_DELAY)

                createCacheRequestAndReturn()
            }
        }


    }


    suspend fun handleNetworkCall(response: GenericApiResponse<ResponseObject>?) {
        when (response) {

            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }

            is ApiErrorResponse -> {
                Log.e(TAG, "NetworkBoundResource: ${response.errorMessage}")
                onErrorReturn(response.errorMessage, true, false)
            }

            is ApiEmptyResponse -> {
                Log.e(TAG, "NetworkBoundResource: Request returned nothing (HTTP 204")
                onErrorReturn(ERROR_UNKNOWN, true, false)
            }

        }
    }

    fun onCompleteJob(dataState: DataState<ViewStateType>) {

        GlobalScope.launch(Main) {
            job.complete()
            setValue(dataState)
        }

    }

    private fun setValue(dataState: DataState<ViewStateType>) {
        result.value = dataState
    }

    fun onErrorReturn(errorMessage: String?, shouldUseDialog: Boolean, shouldeUseToast: Boolean) {

        var msg = errorMessage
        var useDialog = shouldUseDialog
        var responseType: ResponseType = ResponseType.None()

        if (msg == null) {
            msg = ERROR_UNKNOWN
        } else if (ErrorHandling.isNetworkError(msg)) {
            msg = ERROR_CHECK_NETWORK_CONNECTION
            useDialog = false
        }

        if (shouldeUseToast) {
            responseType = ResponseType.Toast()
        }

        if (useDialog) {
            responseType = ResponseType.Dialog()
        }

        onCompleteJob(
            DataState.error(
                response = Response(
                    message = msg,
                    responseType = responseType
                )
            )
        )

    }

    @UseExperimental(InternalCoroutinesApi::class)
    private fun initNewJob(): Job {

        Log.d(TAG, "initNewJob: called...")

        job = Job()
        job.invokeOnCompletion(
            onCancelling = true,
            invokeImmediately = true,
            handler = object : CompletionHandler {

                override fun invoke(cause: Throwable?) {
                    if (job.isCancelled) {
                        Log.e(TAG, "NetworkBoundResource: Job has been cancelled.")
                        cause?.let {
                            onErrorReturn(
                                it.message,
                                shouldUseDialog = false,
                                shouldeUseToast = true
                            )
                        } ?: onErrorReturn(
                            ERROR_UNKNOWN,
                            shouldUseDialog = false,
                            shouldeUseToast = true
                        )
                    } else if (job.isCompleted) {
                        Log.e(TAG, "NetworkBoundResource: Job has been completed...")

                    }
                }

            })

        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>

    abstract suspend fun createCacheRequestAndReturn()

    abstract suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    abstract fun setJob(job: Job)

}