package com.anonymous.prohiking.data.utils

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T): ResultWrapper<T>()
    data class Error(val error: ErrorType): ResultWrapper<Nothing>()
}

sealed class ErrorType {
    object BadRequest: ErrorType()
    object Network: ErrorType()
    object NotFound: ErrorType()
    object Unauthorized: ErrorType()
    data class OtherHttp(val code: Int): ErrorType()
    data class Other(val throwable: Throwable): ErrorType()
}