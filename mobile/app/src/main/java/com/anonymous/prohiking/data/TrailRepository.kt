package com.anonymous.prohiking.data

import android.content.Context
import com.anonymous.prohiking.data.network.PointApiModel
import com.anonymous.prohiking.data.network.TrailApiModel
import com.anonymous.prohiking.data.network.ProHikingApiService
import com.anonymous.prohiking.data.network.utils.enforceLogin
import com.anonymous.prohiking.data.network.utils.safeApiCall
import com.anonymous.prohiking.data.network.utils.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface TrailRepository {
    suspend fun getTrailById(id: Int): ApiResult<TrailApiModel>
    suspend fun getTrailPath(id: Int): ApiResult<List<PointApiModel>>
    suspend fun searchTrails(
        limit: Int,
        offset: Int,
        name: String,
        minLength: Double,
        maxLength: Double,
        centerLat: Double,
        centerLon: Double,
        radius: Double
    ): ApiResult<List<TrailApiModel>>
}

class DefaultTrailRepository(
    private val context: Context,
    private val proHikingApiService: ProHikingApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): TrailRepository {
    override suspend fun getTrailById(id: Int): ApiResult<TrailApiModel> {
        return enforceLogin(context) { safeApiCall(dispatcher) { proHikingApiService.getTrailById(id) } }
    }

    override suspend fun getTrailPath(id: Int): ApiResult<List<PointApiModel>> {
        return enforceLogin(context) { safeApiCall(dispatcher) { proHikingApiService.getTrailPath(id) } }
    }

    override suspend fun searchTrails(
        limit: Int,
        offset: Int,
        name: String,
        minLength: Double,
        maxLength: Double,
        centerLat: Double,
        centerLon: Double,
        radius: Double
    ): ApiResult<List<TrailApiModel>> {
        return enforceLogin(context) { safeApiCall(dispatcher) { proHikingApiService.searchTrails(
            limit,
            offset,
            name,
            "${minLength},${maxLength}",
            "${centerLat},${centerLon}",
            radius
        ) } }
    }
}