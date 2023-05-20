package com.anonymous.prohiking.data.network

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDataApiModel(
    val weather: List<WeatherDataApiModelWeather>
)

@Serializable
data class WeatherDataApiModelWeather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)