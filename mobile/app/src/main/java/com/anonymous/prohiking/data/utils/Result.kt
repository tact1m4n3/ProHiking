package com.anonymous.prohiking.data.utils

sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val error: ErrorType): Result<Nothing>()
}

sealed class ErrorType {
    object BadRequest: ErrorType()
    object Network: ErrorType()
    object NotFound: ErrorType()
    object Unauthorized: ErrorType()
    data class OtherHttp(val code: Int): ErrorType()
    data class Other(val throwable: Throwable): ErrorType()
}