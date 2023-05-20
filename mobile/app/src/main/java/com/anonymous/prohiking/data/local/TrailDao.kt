package com.anonymous.prohiking.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrailDao {
    @Query("SELECT * from trails WHERE id = :id")
    fun getTrailById(id: Int): Flow<TrailEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trail: TrailEntity)

    @Delete
    suspend fun delete(trail: TrailEntity)
}