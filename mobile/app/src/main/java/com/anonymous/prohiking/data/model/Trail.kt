package com.anonymous.prohiking.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Trail(
    val id: Int,
    val name: String,
    val from: String,
    val to: String,
    val length: Double,
    val symbol: String,
    val point: Point
)

@Serializable
data class Point(
    val lat: Double,
    val lon: Double,
)