package com.anonymous.prohiking.data

import com.anonymous.prohiking.data.local.PointDao
import com.anonymous.prohiking.data.local.PointEntity
import com.anonymous.prohiking.data.local.TrailDao
import com.anonymous.prohiking.data.local.TrailEntity
import kotlinx.coroutines.flow.Flow

interface OfflineTrailRepository {
    fun getTrailById(id: Int): Flow<TrailEntity?>
    fun getTrailPath(id: Int): Flow<List<PointEntity>?>
    suspend fun insertTrail(trail: TrailEntity)
    suspend fun insertTrailPath(path: List<PointEntity>)
    suspend fun deleteTrail(trail: TrailEntity)
    suspend fun deleteTrailPath(path: List<PointEntity>)
}

class DefaultOfflineTrailRepository(
    private val trailDao: TrailDao,
    private val pointDao: PointDao
): OfflineTrailRepository {
    override fun getTrailById(id: Int): Flow<TrailEntity?> = trailDao.getTrailById(id)

    override fun getTrailPath(id: Int): Flow<List<PointEntity>?> = pointDao.getPath(id)

    override suspend fun insertTrail(trail: TrailEntity) = trailDao.insert(trail)

    override suspend fun insertTrailPath(path: List<PointEntity>) = pointDao.insertPath(path)

    override suspend fun deleteTrail(trail: TrailEntity) = trailDao.delete(trail)

    override suspend fun deleteTrailPath(path: List<PointEntity>) = pointDao.deletePath(path)
}
