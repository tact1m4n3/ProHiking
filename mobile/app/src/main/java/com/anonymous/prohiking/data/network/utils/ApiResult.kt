package com.anonymous.prohiking.data.network.utils

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T): ApiResult<T>()
    data class Error(val error: ErrorType): ApiResult<Nothing>()
}

sealed class ErrorType {
    object BadRequest: ErrorType()
    object Network: ErrorType()
    object NotFound: ErrorType()
    object Unauthorized: ErrorType()
    data class OtherHttp(val code: Int): ErrorType()
    data class Other(val throwable: Throwable): ErrorType()
}