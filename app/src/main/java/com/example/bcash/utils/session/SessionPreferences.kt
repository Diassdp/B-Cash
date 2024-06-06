package com.example.bcash.utils.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.bcash.model.SessionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getSession(): Flow<SessionModel> {
        return dataStore.data.map { preferences ->
            SessionModel(
                preferences[USER_ID_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[STATUS_KEY] ?: false,
            )
        }
    }

    suspend fun saveSession(session: SessionModel) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = session.userId
            preferences[TOKEN_KEY] = session.token
            preferences[NAME_KEY] = session.name
            preferences[STATUS_KEY] = session.statusLogin
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATUS_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private const val TAG = "SessionPreferences"

        @Volatile
        private var INSTANCE: SessionPreferences? = null
        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATUS_KEY = booleanPreferencesKey("status")

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}