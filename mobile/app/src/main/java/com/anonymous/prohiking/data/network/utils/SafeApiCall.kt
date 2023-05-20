package com.anonymous.prohiking.data.network.utils

import com.anonymous.prohiking.data.network.utils.ApiResult
import com.anonymous.prohiking.data.network.utils.ErrorType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ApiResult<T> {
    return withContext(dispatcher) {
        try {
            ApiResult.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            ApiResult.Error(
                when (throwable) {
                    is IOException -> ErrorType.Network
                    is HttpException -> {
                        when (throwable.code()) {
                            HttpURLConnection.HTTP_BAD_REQUEST -> ErrorType.BadRequest
                            HttpURLConnection.HTTP_NOT_FOUND -> ErrorType.NotFound
                            HttpURLConnection.HTTP_UNAUTHORIZED -> ErrorType.Unauthorized
                            else -> ErrorType.OtherHttp(throwable.code())
                        }
                    }
                    else -> {
                        ErrorType.Other(throwable)
                    }
                }
            )
        }
    }
}