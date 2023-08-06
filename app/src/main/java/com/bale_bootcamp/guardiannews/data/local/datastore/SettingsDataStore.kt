package com.bale_bootcamp.guardiannews.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException

class SettingsDataStore(private val context: Context) {
    private val TAG = "SettingsDataStore"

    val itemCountFlow: suspend () -> Flow<String> = suspend { getPrefFlow(ITEM_COUNT) }
    val orderByFlow: suspend () -> Flow<String> = suspend { getPrefFlow(ORDER_BY) }
    val fromDateFlow: suspend () -> Flow<String> = suspend { getPrefFlow(FROM_DATE) }
    val colorThemeFlow: suspend () -> Flow<Int> = suspend { getPrefFlow(COLOR_THEME) }
    val fontSizeFlow: suspend () -> Flow<Int> = suspend { getPrefFlow(FONT_SIZE) }


    suspend fun saveItemCount(itemCount: String) = savePref(ITEM_COUNT, itemCount)
    suspend fun saveOrderBy(orderBy: String) = savePref(ORDER_BY, orderBy)
    suspend fun saveFromDate(fromDate: String) = savePref(FROM_DATE, fromDate)
    suspend fun saveColorTheme(colorTheme: Int) = savePref(COLOR_THEME, colorTheme)
    suspend fun saveFontSize(fontSize: Int) = savePref(FONT_SIZE, fontSize)

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        private val ITEM_COUNT = stringPreferencesKey("item_count")
        private val ORDER_BY = stringPreferencesKey("order_by")
        private val FROM_DATE = stringPreferencesKey("from_date")
        private val COLOR_THEME = intPreferencesKey("color_theme")
        private val FONT_SIZE = intPreferencesKey("font_size")
    }

    private suspend fun <T> savePref(key: Preferences.Key<T>, value: T) {
        Log.d(TAG, "savePref: key: ${key.name}, value: $value")
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }

    private suspend fun <T> getPrefFlow(key: Preferences.Key<T>): Flow<T> =
        context.dataStore.data
            .catch {exception ->
                if(exception is IOException) {
                    Log.e(TAG, "Error reading preferences", exception)
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map {
                Log.d(TAG, "getPrefFlow: key: ${key.name}, value: ${it[key]}")
                it[key] ?: throw IllegalStateException("No value found for key ${key.name}")
            }
}