package com.anonymous.prohiking.data

import android.content.Context
import com.anonymous.prohiking.data.model.Point
import com.anonymous.prohiking.data.model.Trail
import com.anonymous.prohiking.data.network.ProHikingApiService
import com.anonymous.prohiking.data.network.utils.enforceLogin
import com.anonymous.prohiking.data.network.utils.safeApiCall
import com.anonymous.prohiking.data.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface TrailRepository {
    suspend fun getTrailById(id: Int): Result<Trail>
    suspend fun getTrailPath(id: Int): Result<List<Point>>
    suspend fun searchTrails(
        limit: Int,
//        offset: Int,
        name: String,
        minLength: Double,
        maxLength: Double,
        centerLat: Double,
        centerLon: Double,
        radius: Double
    ): Result<List<Trail>>
}

class DefaultTrailRepository(
    private val context: Context,
    private val proHikingApiService: ProHikingApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): TrailRepository {
    override suspend fun getTrailById(id: Int): Result<Trail> {
        return enforceLogin(context) { safeApiCall(dispatcher) { proHikingApiService.getTrailById(id) } }
    }

    override suspend fun getTrailPath(id: Int): Result<List<Point>> {
        return enforceLogin(context) { safeApiCall(dispatcher) { proHikingApiService.getTrailPath(id) } }
    }

    override suspend fun searchTrails(
        limit: Int,
//        offset: Int,
        name: String,
        minLength: Double,
        maxLength: Double,
        centerLat: Double,
        centerLon: Double,
        radius: Double
    ): Result<List<Trail>> {
        return enforceLogin(context) { safeApiCall(dispatcher) { proHikingApiService.searchTrails(
            limit,
//            offset,
            name,
            "${minLength},${maxLength}",
            "${centerLat},${centerLon}",
            radius
        ) } }
    }
}