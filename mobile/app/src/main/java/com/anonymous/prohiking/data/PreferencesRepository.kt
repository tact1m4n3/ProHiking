package com.anonymous.prohiking.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PreferencesRepository {
    val loggedIn: Flow<Boolean>
    val username: Flow<String>
    val password: Flow<String>

    val currentTrailId: Flow<Int>

    suspend fun updateLoggedIn(value: Boolean)
    suspend fun updateUsernameAndPassword(username: String, password: String)
    suspend fun updateCurrentTrailId(trailId: Int)
}

class DefaultPreferencesRepository(private val dataStore: DataStore<Preferences>): PreferencesRepository {
    private companion object {
        val LOGGED_IN = booleanPreferencesKey("logged_in")
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")

        val CURRENT_TRAIL_ID = intPreferencesKey("current_trail_id")
    }

    override val loggedIn = dataStore.data.map { preferences ->
        preferences[LOGGED_IN] ?: false
    }

    override val username = dataStore.data.map { preferences ->
        preferences[USERNAME] ?: ""
    }

    override val password = dataStore.data.map { preferences ->
        preferences[PASSWORD] ?: ""
    }

    override val currentTrailId = dataStore.data.map { preferences ->
        preferences[CURRENT_TRAIL_ID] ?: -1
    }

    override suspend fun updateLoggedIn(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOGGED_IN] = value
        }
    }

    override suspend fun updateUsernameAndPassword(username: String, password: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = username
            preferences[PASSWORD] = password
        }
    }

    override suspend fun updateCurrentTrailId(trailId: Int) {
        dataStore.edit { preferences ->
            preferences[CURRENT_TRAIL_ID] = trailId
        }
    }
}