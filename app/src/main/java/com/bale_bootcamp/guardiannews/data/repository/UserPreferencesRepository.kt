package com.bale_bootcamp.guardiannews.data.repository

import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bale_bootcamp.guardiannews.data.local.datastore.SettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserPreferencesRepository(private val settingsDataStore: SettingsDataStore) {
    private val TAG = "UserPreferencesRepository"

    companion object {
        @Volatile private var instance: UserPreferencesRepository? = null

        fun getInstance(settingsDataStore: SettingsDataStore) =
            instance ?: synchronized(this) {
                instance ?: UserPreferencesRepository(settingsDataStore).also { instance = it }
            }
    }


    private object keys {
        val ITEM_COUNT = intPreferencesKey("item_count")
        val ORDER_BY = stringPreferencesKey("order_by")
        val FROM_DATE = stringPreferencesKey("from_date")
        val COLOR_THEME = stringPreferencesKey("color_theme")
        val FONT_SIZE = stringPreferencesKey("font_size")
    }


    suspend fun getItemCount(): Flow<Int> = settingsDataStore.getPrefFlow(keys.ITEM_COUNT)
    suspend fun getOrderBy(): Flow<String> = settingsDataStore.getPrefFlow(keys.ORDER_BY)
    suspend fun getFromDate(): Flow<String> = settingsDataStore.getPrefFlow(keys.FROM_DATE)
    suspend fun getColorTheme(): Flow<String> = settingsDataStore.getPrefFlow(keys.COLOR_THEME)
    suspend fun getFontSize(): Flow<String> = settingsDataStore.getPrefFlow(keys.FONT_SIZE)

    suspend fun saveItemCount(itemCount: Int) = settingsDataStore.savePref(keys.ITEM_COUNT, itemCount)
    suspend fun saveOrderBy(orderBy: String) = settingsDataStore.savePref(keys.ORDER_BY, orderBy)
    suspend fun saveFromDate(fromDate: String) = settingsDataStore.savePref(keys.FROM_DATE, fromDate)
    suspend fun saveColorTheme(colorTheme: String) {
        Log.d(TAG, "saveColorTheme: colorTheme: $colorTheme")
        settingsDataStore.savePref(keys.COLOR_THEME, colorTheme)
    }
    suspend fun saveFontSize(fontSize: String) = settingsDataStore.savePref(keys.FONT_SIZE, fontSize)

}