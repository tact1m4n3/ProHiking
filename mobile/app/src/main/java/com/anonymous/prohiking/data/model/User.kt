package com.anonymous.prohiking.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String,
    val email: String,
)