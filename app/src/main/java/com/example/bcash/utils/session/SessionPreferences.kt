package com.example.bcash.utils.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.bcash.model.SessionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class SessionPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getSession(): Flow<SessionModel> {
        return dataStore.data.map { preferences ->
            SessionModel(
                preferences[USER_ID_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[STATUS_KEY] ?: false,
                preferences[LOGIN_TIMESTAMP_KEY] ?: 0L // Include loginTimestamp
            )
        }
    }

    suspend fun saveSession(session: SessionModel) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = session.userId
            preferences[TOKEN_KEY] = session.token
            preferences[NAME_KEY] = session.name
            preferences[STATUS_KEY] = session.statusLogin
            preferences[LOGIN_TIMESTAMP_KEY] = session.loginTimestamp // Save loginTimestamp
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATUS_KEY] = true
            preferences[LOGIN_TIMESTAMP_KEY] = System.currentTimeMillis()
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun shouldLogout(): Boolean {
        val currentTime = System.currentTimeMillis()
        val loginTimestamp = dataStore.data.map { preferences ->
            preferences[LOGIN_TIMESTAMP_KEY] ?: 0L
        }.first()
        return currentTime - loginTimestamp > LOGOUT_TIME_LIMIT
    }

    companion object {
        private const val TAG = "SessionPreferences"
        private const val LOGOUT_TIME_LIMIT = 60 * 60 * 1000L // 1 hour in milliseconds

        @Volatile
        private var INSTANCE: SessionPreferences? = null
        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATUS_KEY = booleanPreferencesKey("status")
        private val LOGIN_TIMESTAMP_KEY = longPreferencesKey("loginTimestamp")

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
