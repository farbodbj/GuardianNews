package com.bale_bootcamp.guardiannews.data.repository

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bale_bootcamp.guardiannews.data.local.datastore.SettingsDataStore
import com.bale_bootcamp.guardiannews.ui.settings.model.ColorTheme
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.lang.Exception


private const val TAG = "UserPreferencesRepository"
class SettingsRepository(private val settingsDataStore: SettingsDataStore) {
    private object Keys {
        val ITEM_COUNT = intPreferencesKey("item_count")
        const val ITEM_COUNT_DEFAULT = 10
        val ORDER_BY = stringPreferencesKey("order_by")
        val ORDER_BY_DEFAULT = OrderBy.NEWEST
        val FROM_DATE = stringPreferencesKey("from_date")
        val FROM_DATE_DEFAULT = LocalDate.now().minusDays(1).toString()
        val COLOR_THEME = stringPreferencesKey("color_theme")
        val COLOR_THEME_DEFAULT = ColorTheme.WHITE
        val FONT_SIZE = stringPreferencesKey("font_size")
        const val FONT_SIZE_DEFAULT = "medium"
    }
    object KeyDefaults {
        val defaultMap = mapOf<String, Any>(
            Keys.ITEM_COUNT.name to Keys.ITEM_COUNT_DEFAULT,
            Keys.ORDER_BY.name to Keys.ORDER_BY_DEFAULT.toString(),
            Keys.FROM_DATE.name to Keys.FROM_DATE_DEFAULT,
            Keys.COLOR_THEME.name to Keys.COLOR_THEME_DEFAULT.toString(),
            Keys.FONT_SIZE.name to Keys.FONT_SIZE_DEFAULT)
    }


    private suspend fun <T> setValueIfNotExists(key: Preferences.Key<T>, defaultValue: T) {
        try {
            val value = settingsDataStore.getPrefFlow(key).firstOrNull()
            if (value == null) {
                Log.d(TAG, "setValueIfNotExists: key: ${key.name}, value: $defaultValue")
                settingsDataStore.savePref(key, defaultValue)
            }
        } catch (e: Exception) {
            Log.e(TAG, "setValueIfNotExists: Error occurred while retrieving value for key ${key.name}: ${e.message}")
        }
    }

    init {
        CoroutineScope(Dispatchers.Default).launch {
            setValueIfNotExists(Keys.ITEM_COUNT, Keys.ITEM_COUNT_DEFAULT)
            setValueIfNotExists(Keys.ORDER_BY, Keys.ORDER_BY_DEFAULT.toString())
            setValueIfNotExists(Keys.FROM_DATE, Keys.FROM_DATE_DEFAULT.toString())
            setValueIfNotExists(Keys.COLOR_THEME, Keys.COLOR_THEME_DEFAULT.toString())
            setValueIfNotExists(Keys.FONT_SIZE, Keys.FONT_SIZE_DEFAULT.toString())
        }
        Log.d(TAG, "default values added")
    }

    suspend fun getItemCount(): Flow<Int> = settingsDataStore.getPrefFlow(Keys.ITEM_COUNT)
    suspend fun getOrderBy(): Flow<String> = settingsDataStore.getPrefFlow(Keys.ORDER_BY)
    suspend fun getFromDate(): Flow<String> = settingsDataStore.getPrefFlow(Keys.FROM_DATE)
    suspend fun getColorTheme(): Flow<String> = settingsDataStore.getPrefFlow(Keys.COLOR_THEME)
    suspend fun getFontSize(): Flow<String> = settingsDataStore.getPrefFlow(Keys.FONT_SIZE)
    suspend fun saveItemCount(itemCount: Int) = settingsDataStore.savePref(Keys.ITEM_COUNT, itemCount)
    suspend fun saveOrderBy(orderBy: String) = settingsDataStore.savePref(Keys.ORDER_BY, orderBy)
    suspend fun saveFromDate(fromDate: String) = settingsDataStore.savePref(Keys.FROM_DATE, fromDate)
    suspend fun saveColorTheme(colorTheme: String) = settingsDataStore.savePref(Keys.COLOR_THEME, colorTheme)
    suspend fun saveFontSize(fontSize: String) = settingsDataStore.savePref(Keys.FONT_SIZE, fontSize)

    companion object {
        @Volatile private var instance: SettingsRepository? = null
        fun getInstance(settingsDataStore: SettingsDataStore) =
            instance ?: synchronized(this) {
                instance ?: SettingsRepository(settingsDataStore).also { instance = it }
            }
    }
}