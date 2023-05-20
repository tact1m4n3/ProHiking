package com.anonymous.prohiking.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PointDao {
    @Query("SELECT * FROM points WHERE trailId = :trailId")
    fun getPath(trailId: Int): Flow<List<PointEntity>>

    @Insert
    suspend fun insertPath(path: List<PointEntity>)

    @Delete
    suspend fun deletePath(path: List<PointEntity>)

    @Query("DELETE FROM points WHERE trailId = :trailId")
    suspend fun deletePath(trailId: Int)
}