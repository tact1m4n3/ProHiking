package com.anonymous.prohiking.data.network.utils

import com.anonymous.prohiking.data.utils.Result
import com.anonymous.prohiking.data.utils.ErrorType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): Result<T> {
    return withContext(dispatcher) {
        try {
            Result.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Result.Error(
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