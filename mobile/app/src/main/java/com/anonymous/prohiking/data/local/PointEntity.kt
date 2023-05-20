package com.anonymous.prohiking.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "points", foreignKeys = [
    ForeignKey(
        entity = TrailEntity::class,
        parentColumns = ["id"],
        childColumns = ["trailId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class PointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val trailId: Int,
    val lat: Double,
    val lon: Double,
)