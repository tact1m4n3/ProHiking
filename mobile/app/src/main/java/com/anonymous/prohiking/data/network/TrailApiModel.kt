package com.anonymous.prohiking.data.network

import com.anonymous.prohiking.data.local.TrailEntity
import kotlinx.serialization.Serializable

@Serializable
data class TrailApiModel(
    val id: Int,
    val name: String,
    val from: String,
    val to: String,
    val length: Double,
    val symbol: String,
    val point: PointApiModel
) {
    fun toEntity(): TrailEntity = TrailEntity(
        id, name, from, to, length, symbol
    )
}