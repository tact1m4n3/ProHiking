package com.anonymous.prohiking.data.network.utils

import android.content.Context
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.network.utils.ErrorType
import com.anonymous.prohiking.data.network.utils.ApiResult
import kotlinx.coroutines.flow.first

suspend fun <T> enforceLogin(context: Context, safeApiCall: suspend () -> ApiResult<T>): ApiResult<T> {
    return when (val result = safeApiCall.invoke()) {
        is ApiResult.Success -> result
        is ApiResult.Error -> {
            when (result.error) {
                ErrorType.Unauthorized -> loginAndExec(context, safeApiCall)
                else -> result
            }
        }
    }
}

private suspend fun <T> loginAndExec(context: Context, safeApiCall: suspend () -> ApiResult<T>): ApiResult<T> {
    val application = context as ProHikingApplication
    val userRepository = application.userRepository
    val preferencesRepository = application.preferencesRepository

    val userId = preferencesRepository.userId.first()
    if (userId == -1) {
        return ApiResult.Error(ErrorType.Unauthorized)
    }

    val username = preferencesRepository.username.first()
    val password = preferencesRepository.password.first()

    return when (val result = userRepository.loginUser(username, password)) {
        is ApiResult.Success -> safeApiCall.invoke()
        is ApiResult.Error -> result
    }
}