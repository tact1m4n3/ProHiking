package com.anonymous.prohiking.data.network

import com.anonymous.prohiking.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

const val WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather/"
const val WEATHER_ICON_API_URL = "https://openweathermap.org/img/wn/"

interface WeatherApiService {
    @GET("?appid=${BuildConfig.WEATHER_API_KEY}&units=metric")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): WeatherDataApiModel
}

fun initWeatherApiService(): WeatherApiService {
    @OptIn(ExperimentalSerializationApi::class)
    val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(WEATHER_API_URL)
        .build()

    val weatherApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    return weatherApiService
}

fun getWeatherIconUrl(icon: String): String = "${WEATHER_ICON_API_URL}${icon}@2x.png"