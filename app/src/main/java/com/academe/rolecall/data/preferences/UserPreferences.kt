package com.academe.rolecall.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

interface UserPreferences {
    val demoModeFlow: Flow<Boolean>
    val authenticatedFlow: Flow<Boolean>
    suspend fun setDemoMode(enabled: Boolean)
    suspend fun setAuthenticated(enabled: Boolean)
    suspend fun clear()
}

@Singleton
class UserPreferencesImpl @Inject constructor(
    private val context: Context
) : UserPreferences {
    private val demoModeKey = booleanPreferencesKey("demo_mode")
    private val authenticatedKey = booleanPreferencesKey("authenticated")

    override val demoModeFlow: Flow<Boolean> = getValue(demoModeKey, false)
    override val authenticatedFlow: Flow<Boolean> = getValue(authenticatedKey, false)

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

    override suspend fun setDemoMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[demoModeKey] = enabled
        }
    }

    override suspend fun setAuthenticated(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[authenticatedKey] = enabled
        }
    }

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
