package com.anonymous.prohiking.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trails")
data class TrailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val from: String,
    val to: String,
    val length: Double,
    val symbol: String,
)