package com.baldystudios.androidjetpackmviadvanced.repository

import com.baldystudios.androidjetpackmviadvanced.util.*
import com.baldystudios.androidjetpackmviadvanced.util.ApiResult.*
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.CACHE_ERROR_TIMEOUT
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.CACHE_TIMEOUT
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.NETWORK_ERROR_TIMEOUT
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.NETWORK_TIMEOUT
import com.baldystudios.androidjetpackmviadvanced.util.Constants.Companion.UNKNOWN_ERROR
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

private val TAG: String = "AppDebug"

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(NETWORK_TIMEOUT){
                Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    GenericError(code, NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    GenericError(
                        code,
                        errorResponse
                    )
                }
                else -> {
                    GenericError(
                        null,
                        UNKNOWN_ERROR
                    )
                }
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
): CacheResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT){
                CacheResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is TimeoutCancellationException -> {
                    CacheResult.GenericError(CACHE_ERROR_TIMEOUT)
                }
                else -> {
                    CacheResult.GenericError(UNKNOWN_ERROR)
                }
            }
        }
    }
}


fun <ViewState> emitError(
    message: String,
    uiComponentType: UIComponentType,
    stateEvent: StateEvent?
): Flow<DataState<ViewState>> = flow{
    emit(
        DataState.error(
            response = Response(
                message = "${stateEvent?.errorInfo()}\n\nReason: ${message}",
                uiComponentType = uiComponentType,
                messageType = MessageType.Error()
            ),
            stateEvent = stateEvent
        )
    )
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.toString()
    } catch (exception: Exception) {
        UNKNOWN_ERROR
    }
}