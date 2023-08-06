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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.IOException
import java.lang.Thread.State
import kotlin.coroutines.coroutineContext

class SettingsDataStore(private val context: Context) {
    private val TAG = "SettingsDataStore"

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        private val ITEM_COUNT = intPreferencesKey("item_count")
        private val ORDER_BY = stringPreferencesKey("order_by")
        private val FROM_DATE = stringPreferencesKey("from_date")
        private val COLOR_THEME = stringPreferencesKey("color_theme")
        private val FONT_SIZE = stringPreferencesKey("font_size")
    }

    init {
        Log.d(TAG, "init: ")
        CoroutineScope(Dispatchers.IO).launch {
            initSettings()
        }
    }

    suspend fun getItemCount(): Flow<Int> = getPrefFlow(ITEM_COUNT)
    suspend fun getOrderBy(): Flow<String> = getPrefFlow(ORDER_BY)
    suspend fun getFromDate(): Flow<String> = getPrefFlow(FROM_DATE)
    suspend fun getColorTheme(): Flow<String> = getPrefFlow(COLOR_THEME)
    suspend fun getFontSize(): Flow<String> = getPrefFlow(FONT_SIZE)

    suspend fun saveItemCount(itemCount: Int) = savePref(ITEM_COUNT, itemCount)
    suspend fun saveOrderBy(orderBy: String) = savePref(ORDER_BY, orderBy)
    suspend fun saveFromDate(fromDate: String) = savePref(FROM_DATE, fromDate)
    suspend fun saveColorTheme(colorTheme: String) = savePref(COLOR_THEME, colorTheme)
    suspend fun saveFontSize(fontSize: String) = savePref(FONT_SIZE, fontSize)


    private suspend fun initSettings() = context.dataStore.edit { settings ->
            settings[ITEM_COUNT] = 10
            settings[ORDER_BY] = "newest"
            settings[FROM_DATE] = ""
            settings[COLOR_THEME] = "white"
            settings[FONT_SIZE] = "medium"
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