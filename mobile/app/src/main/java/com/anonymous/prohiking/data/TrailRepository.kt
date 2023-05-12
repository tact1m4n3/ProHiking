package com.anonymous.prohiking.data

import com.anonymous.prohiking.data.model.Point
import com.anonymous.prohiking.data.model.Trail
import com.anonymous.prohiking.data.network.ProHikingApiService
import com.anonymous.prohiking.data.network.utils.enforceLogin
import com.anonymous.prohiking.data.network.utils.safeApiCall
import com.anonymous.prohiking.data.utils.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TrailRepository(
    private val proHikingApiService: ProHikingApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getTrailById(id: Int): ResultWrapper<Trail> {
        return enforceLogin { safeApiCall(dispatcher) { proHikingApiService.getTrailById(id) } }
    }

    suspend fun getTrailPath(id: Int): ResultWrapper<List<Point>> {
        return enforceLogin { safeApiCall(dispatcher) { proHikingApiService.getTrailPath(id) } }
    }

    suspend fun searchTrails(
        limit: Int,
        offset: Int,
        name: String,
        minLength: Double,
        maxLength: Double,
        bottomLeftLat: Double,
        bottomLeftLon: Double,
        topRightLat: Double,
        topRightLon: Double
    ): ResultWrapper<List<Trail>> {
        return enforceLogin { safeApiCall(dispatcher) { proHikingApiService.searchTrails(
            limit,
            offset,
            name,
            "${minLength},${maxLength}",
            "${bottomLeftLat},${bottomLeftLon},${topRightLat},${topRightLon}"
        ) } }
    }
}