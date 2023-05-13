package com.anonymous.prohiking.data.network.utils

import android.content.Context
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.utils.ErrorType
import com.anonymous.prohiking.data.utils.Result
import kotlinx.coroutines.flow.first

suspend fun <T> enforceLogin(context: Context, safeApiCall: suspend () -> Result<T>): Result<T> {
    return when (val result = safeApiCall.invoke()) {
        is Result.Success -> result
        is Result.Error -> {
            when (result.error) {
                ErrorType.Unauthorized -> loginAndExec(context, safeApiCall)
                else -> result
            }
        }
    }
}

private suspend fun <T> loginAndExec(context: Context, safeApiCall: suspend () -> Result<T>): Result<T> {
    val application = context as ProHikingApplication
    val userRepository = application.userRepository
    val preferencesRepository = application.preferencesRepository

    val loggedIn = preferencesRepository.loggedIn.first()
    if (!loggedIn) {
        return Result.Error(ErrorType.Unauthorized)
    }

    val username = preferencesRepository.username.first()
    val password = preferencesRepository.password.first()

    return when (val result = userRepository.loginUser(username, password)) {
        is Result.Success -> safeApiCall.invoke()
        is Result.Error -> result
    }
}