package com.anonymous.prohiking.data.network

import com.anonymous.prohiking.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

private const val WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather"

interface WeatherApiService {
    suspend fun getWeatherData(): WeatherDataApiModel?
}

fun initWeatherApiService(): ProHikingApiService {
    @OptIn(ExperimentalSerializationApi::class)
    val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.PROHIKING_API_URL)
        .build()

    val proHikingApiService: ProHikingApiService by lazy {
        retrofit.create(ProHikingApiService::class.java)
    }

    return proHikingApiService
}
