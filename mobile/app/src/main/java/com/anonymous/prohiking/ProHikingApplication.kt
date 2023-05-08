package com.anonymous.prohiking

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.anonymous.prohiking.data.PreferencesRepository
import com.anonymous.prohiking.data.UserRepository
import com.anonymous.prohiking.data.network.ProHikingApiService
import com.anonymous.prohiking.data.network.initProHikingApiService

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

    lateinit var userRepository: UserRepository
    lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        proHikingApiService = initProHikingApiService()
        userRepository = UserRepository(proHikingApiService)
        preferencesRepository = PreferencesRepository(dataStore)
    }
}