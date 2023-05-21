package com.anonymous.prohiking.data

import com.anonymous.prohiking.data.network.AltitudeApiModel
import com.anonymous.prohiking.data.network.AltitudeApiService
import com.anonymous.prohiking.data.network.utils.ApiResult
import com.anonymous.prohiking.data.network.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface AltitudeRepository {
    suspend fun getAltitude(lat: List<Double>, lon: List<Double>): ApiResult<AltitudeApiModel>
}


class DefaultAltitudeRepository(
    private val altitudeApiService: AltitudeApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): AltitudeRepository {
    override suspend fun getAltitude(lat: List<Double>, lon: List<Double>): ApiResult<AltitudeApiModel>
            = safeApiCall(dispatcher) { altitudeApiService.getAltitude(lat, lon) }
}