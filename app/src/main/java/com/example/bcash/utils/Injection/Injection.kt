package com.example.bcash.utils.Injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.bcash.service.api.ApiConfig
import com.example.bcash.service.repository.Repository
import com.example.bcash.utils.session.SessionPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val session = SessionPreferences.getInstance(context.dataStore)
        return Repository(context, session, apiService)
    }
}
