package com.anonymous.prohiking.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDataApiModel(
    val base: String,
    val clouds: WeatherDataApiModelClouds,
    val cod: Int,
    val coord: WeatherDataApiModelCoord,
    val dt: Int,
    val id: Int,
    val main: WeatherDataApiModelMain,
    val name: String,
    val rain: WeatherDataApiModelRain? = null,
    val sys: WeatherDataApiModelSys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherDataApiModelWeather>,
    val wind: WeatherDataApiModelWind
)

@Serializable
data class WeatherDataApiModelClouds(
    val all: Int
)

@Serializable
data class WeatherDataApiModelCoord(
    val lat: Double,
    val lon: Double
)

@Serializable
data class WeatherDataApiModelMain(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)

@Serializable
data class WeatherDataApiModelRain(
    @SerialName("1h")
    val h: Double
)

@Serializable
data class WeatherDataApiModelSys(
    val country: String,
    val id: Int = -1,
    val sunrise: Int,
    val sunset: Int,
    val type: Int = -1
)

@Serializable
data class WeatherDataApiModelWeather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

@Serializable
data class WeatherDataApiModelWind(
    val deg: Int,
    val gust: Double,
    val speed: Double
)