package com.baldystudios.androidjetpackmviadvanced.repository

import com.baldystudios.androidjetpackmviadvanced.util.*
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.NETWORK_ERROR
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.UNKNOWN_ERROR
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


abstract class NetworkBoundResource<NetworkObj, CacheObj, ViewState>
constructor(
    private val dispatcher: CoroutineDispatcher,
    private val stateEvent: StateEvent,
    private val apiCall: suspend () -> NetworkObj?,
    private val cacheCall: suspend () -> CacheObj?
) {

    val result: Flow<DataState<ViewState>> = flow {
// ****** STEP 1: VIEW CACHE ******
        emitCache(markJobComplete = false)

        // ****** STEP 2: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
        val apiResult = safeApiCall(dispatcher) { apiCall }

        when (apiResult) {
            is ApiResult.GenericError -> {
                emitError<ViewState>(
                    apiResult.errorMessage?.let { it } ?: UNKNOWN_ERROR,
                    UIComponentType.Dialog(),
                    stateEvent
                )
            }

            is ApiResult.NetworkError -> {
                emitError<ViewState>(
                    NETWORK_ERROR,
                    UIComponentType.Dialog(),
                    stateEvent
                )
            }

            is ApiResult.Success -> {
                if (apiResult.value == null) {
                    emitError<ViewState>(
                        UNKNOWN_ERROR,
                        UIComponentType.Dialog(),
                        stateEvent
                    )
                } else {
                    updateCache(apiResult.value as NetworkObj)
                }
            }
        }

        // ****** STEP 3: VIEW CACHE and MARK JOB COMPLETED ******
        emitCache(markJobComplete = true)

    }

    private fun emitCache(markJobComplete: Boolean): Flow<DataState<ViewState>> = flow {

        val cacheResult = safeCacheCall(dispatcher) { cacheCall.invoke() }

        var jobCompleteMarker: StateEvent? = null
        if (markJobComplete) {
            jobCompleteMarker = stateEvent
        }
        emit(
            object : CacheResponseHandler<ViewState, CacheObj>(
                response = cacheResult,
                stateEvent = jobCompleteMarker
            ) {
                override fun handleSuccess(resultObj: CacheObj): DataState<ViewState> {
                    return handleCacheSuccess()
                }
            }.result
        )

    }

    abstract fun updateCache(networkObject: NetworkObj)

    abstract fun handleCacheSuccess(): DataState<ViewState> // make sure to return null for stateEvent

}
