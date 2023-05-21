package com.anonymous.prohiking.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AltitudeApiModel(
    val elevation: List<Double>
)