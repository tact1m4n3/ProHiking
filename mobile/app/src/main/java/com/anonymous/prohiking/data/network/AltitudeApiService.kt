package com.anonymous.prohiking.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

const val ALTITUDE_API_URL = "https://api.open-meteo.com/"

interface AltitudeApiService {
    @GET("v1/elevation")
    suspend fun getAltitude(
        @Query("latitude") lat: List<Double>,
        @Query("longitude") lon: List<Double>
    ): AltitudeApiModel
}

fun initAltitudeApiService(): AltitudeApiService {
    @OptIn(ExperimentalSerializationApi::class)
    val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(ALTITUDE_API_URL)
        .build()

    val altitudeApiService: AltitudeApiService by lazy {
        retrofit.create(AltitudeApiService::class.java)
    }

    return altitudeApiService
}