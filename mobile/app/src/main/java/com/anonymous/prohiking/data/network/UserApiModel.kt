package com.anonymous.prohiking.data.network

import kotlinx.serialization.Serializable

@Serializable
data class UserApiModel(
    val id: Int,
    val username: String,
    val email: String,
)