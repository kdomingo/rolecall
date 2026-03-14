package com.academe.rolecall.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigationevent.OnBackCompletedFallback
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(@param:ApplicationContext private val context: Context) {
    private val demoModeKey = booleanPreferencesKey("demo_mode")
    private val authenticatedKey = booleanPreferencesKey("authenticated")

    val demoModeFlow: Flow<Boolean> = getValue(demoModeKey, false)
    val authenticatedFlow: Flow<Boolean> = getValue(authenticatedKey,false)

    @Suppress("SameParameterValue")
    private fun <T> getValue(key: Preferences.Key<T>, fallback: T): Flow<T> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[key] ?: fallback
        }

    suspend fun setDemoMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[demoModeKey] = enabled
        }
    }

    suspend fun setAuthenticated(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[authenticatedKey] = enabled
        }
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
