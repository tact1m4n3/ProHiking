package com.anonymous.prohiking

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.anonymous.prohiking.data.DefaultLocationClient
import com.anonymous.prohiking.data.DefaultOfflineTrailRepository
import com.anonymous.prohiking.data.DefaultPreferencesRepository
import com.anonymous.prohiking.data.DefaultTrailRepository
import com.anonymous.prohiking.data.DefaultUserRepository
import com.anonymous.prohiking.data.DefaultWeatherRepository
import com.anonymous.prohiking.data.LocationClient
import com.anonymous.prohiking.data.OfflineTrailRepository
import com.anonymous.prohiking.data.PreferencesRepository
import com.anonymous.prohiking.data.TrailRepository
import com.anonymous.prohiking.data.UserRepository
import com.anonymous.prohiking.data.WeatherRepository
import com.anonymous.prohiking.data.local.ProHikingDatabase
import com.anonymous.prohiking.data.network.ProHikingApiService
import com.anonymous.prohiking.data.network.WeatherApiService
import com.anonymous.prohiking.data.network.initProHikingApiService
import com.anonymous.prohiking.data.network.initWeatherApiService
import com.google.android.gms.location.LocationServices

class ProHikingApplication: Application() {
    companion object {
        lateinit var instance: ProHikingApplication
            private set
    }

    private val layoutPreferenceName = "layout_preferences"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = layoutPreferenceName
    )

    private lateinit var proHikingApiService: ProHikingApiService
    private lateinit var weatherApiService: WeatherApiService

    lateinit var preferencesRepository: PreferencesRepository
    lateinit var userRepository: UserRepository
    lateinit var trailRepository: TrailRepository
    lateinit var offlineTrailRepository: OfflineTrailRepository
    lateinit var weatherRepository: WeatherRepository

    lateinit var locationClient: LocationClient

    override fun onCreate() {
        super.onCreate()
        instance = this

        proHikingApiService = initProHikingApiService()
        weatherApiService = initWeatherApiService()

        preferencesRepository = DefaultPreferencesRepository(dataStore)
        userRepository = DefaultUserRepository(applicationContext, proHikingApiService)
        trailRepository = DefaultTrailRepository(applicationContext, proHikingApiService)

        val proHikingDatabase = ProHikingDatabase.getDatabase(applicationContext)
        offlineTrailRepository = DefaultOfflineTrailRepository(
            proHikingDatabase.trailDao(),
            proHikingDatabase.pointDao()
        )

        weatherRepository = DefaultWeatherRepository(weatherApiService)

        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }
}