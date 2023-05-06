package com.anonymous.prohiking.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: Int?,
    val username: String,
    val email: String,
)