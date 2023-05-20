package com.anonymous.prohiking.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TrailEntity::class, PointEntity::class], version = 1, exportSchema = false)
abstract class ProHikingDatabase: RoomDatabase() {
    abstract fun trailDao(): TrailDao

    abstract fun pointDao(): PointDao

    companion object {
        @Volatile
        private var Instance: ProHikingDatabase? = null

        fun getDatabase(context: Context): ProHikingDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ProHikingDatabase::class.java, "pro_hiking_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }

}