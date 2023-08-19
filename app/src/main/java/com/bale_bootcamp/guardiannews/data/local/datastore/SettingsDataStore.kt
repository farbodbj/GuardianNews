package com.bale_bootcamp.guardiannews.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.bale_bootcamp.guardiannews.data.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val SETTINGS_PREFERENCES_NAME = "settings_preferences"
private const val TAG = "SettingsDataStore"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SETTINGS_PREFERENCES_NAME)
@Singleton
class SettingsDataStore @Inject constructor(@ApplicationContext val context: Context) {
    suspend fun <T> savePref(key: Preferences.Key<T>, value: T) {
        Log.d(TAG, "savePref: key: ${key.name}, value: $value")
        try {
            context.dataStore.edit { settings -> settings[key] = value }
        } catch (e: IOException) {
            Log.e(TAG, "savePref: ${e.message}")
        }
    }

    fun <T> getPrefFlow(key: Preferences.Key<T>): Flow<T> =
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

}