package com.anonymous.prohiking.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val LOGGED_IN = booleanPreferencesKey("logged_in")
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
    }

    val loggedIn = dataStore.data.map { preferences ->
        preferences[LOGGED_IN] ?: false
    }

    val username = dataStore.data.map { preferences ->
        preferences[USERNAME] ?: ""
    }

    val password = dataStore.data.map { preferences ->
        preferences[PASSWORD] ?: ""
    }

    suspend fun setLoggedIn(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOGGED_IN] = value
        }
    }

    suspend fun setUsernameAndPassword(username: String, password: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = username
            preferences[PASSWORD] = password
        }
    }
}