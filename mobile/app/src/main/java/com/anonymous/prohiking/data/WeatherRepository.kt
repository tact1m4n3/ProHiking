package com.anonymous.prohiking.data

import com.anonymous.prohiking.data.network.WeatherApiService
import com.anonymous.prohiking.data.network.WeatherDataApiModel
import com.anonymous.prohiking.data.network.utils.ApiResult
import com.anonymous.prohiking.data.network.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, lon: Double): ApiResult<WeatherDataApiModel>
}

class DefaultWeatherRepository(
    private val weatherApiService: WeatherApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): WeatherRepository {
    override suspend fun getWeatherData(lat: Double, lon: Double): ApiResult<WeatherDataApiModel>
        = safeApiCall(dispatcher) { weatherApiService.getWeatherData(lat, lon) }
}