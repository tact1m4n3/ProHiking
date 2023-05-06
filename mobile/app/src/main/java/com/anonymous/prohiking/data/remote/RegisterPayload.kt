package com.anonymous.prohiking.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class RegisterPayload(
    val username: String,
    val email: String,
    val password: String,
)