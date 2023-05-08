package com.anonymous.prohiking.data.network.utils

import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.utils.ErrorType
import com.anonymous.prohiking.data.utils.ResultWrapper
import kotlinx.coroutines.flow.first

suspend fun <T> enforceLogin(safeApiCall: suspend () -> ResultWrapper<T>): ResultWrapper<T> {
    return when (val result = safeApiCall.invoke()) {
        is ResultWrapper.Success -> result
        is ResultWrapper.Error -> {
            when (result.error) {
                ErrorType.Unauthorized -> loginAndExec(safeApiCall)
                else -> result
            }
        }
    }
}

private suspend fun <T> loginAndExec(safeApiCall: suspend () -> ResultWrapper<T>): ResultWrapper<T> {
    val userRepository = ProHikingApplication.instance.userRepository
    val preferencesRepository = ProHikingApplication.instance.preferencesRepository

    val loggedIn = preferencesRepository.loggedIn.first()
    if (!loggedIn) {
        return ResultWrapper.Error(ErrorType.Unauthorized)
    }

    val username = preferencesRepository.username.first()
    val password = preferencesRepository.password.first()

    return when (val result = userRepository.loginUser(username, password)) {
        is ResultWrapper.Success -> safeApiCall.invoke()
        is ResultWrapper.Error -> result
    }
}