package com.bale_bootcamp.guardiannews.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.bale_bootcamp.guardiannews.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException

private const val SETTINGS_PREFERENCES_NAME = "settings_preferences"
class SettingsDataStore(private val context: Context) {
    private val TAG = "SettingsDataStore"

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SETTINGS_PREFERENCES_NAME)
    }

    suspend fun <T> savePref(key: Preferences.Key<T>, value: T) {
        Log.d(TAG, "savePref: key: ${key.name}, value: $value")
        try {
            context.dataStore.edit { settings -> settings[key] = value }
        } catch (e: IOException) {
            Log.e(TAG, "savePref: ${e.message}")
        }
    }

    suspend fun <T> getPrefFlow(key: Preferences.Key<T>): Flow<T> =
        context.dataStore.data
            .catch {exception ->
                if(exception is IOException) {
                    Log.e(TAG, "IOException while reading preferences:", exception)
                    emit(emptyPreferences())
                }
            }.map {
                Log.d(TAG, "getPrefFlow: key: ${key.name}, value: ${it[key]}")
                it[key] ?: SettingsRepository.KeyDefaults.defaultMap[key.name] as T
            }

    class SettingsDataStoreFactory(private val context: Context) {

        @Volatile private var instance: SettingsDataStore? = null

        fun create(): SettingsDataStore {
            return instance ?: synchronized(this) {
                instance ?: SettingsDataStore(context).also { instance = it }
            }
        }
    }

}