package com.anonymous.prohiking.data.network

import com.anonymous.prohiking.data.local.PointEntity
import kotlinx.serialization.Serializable

@Serializable
data class PointApiModel(
    val lat: Double,
    val lon: Double,
) {
    fun toEntity(trailId: Int): PointEntity = PointEntity(
        0, trailId, lat, lon,
    )
}