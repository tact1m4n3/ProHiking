package com.anonymous.prohiking.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class LoginPayload(
    val username: String,
    val password: String,
)