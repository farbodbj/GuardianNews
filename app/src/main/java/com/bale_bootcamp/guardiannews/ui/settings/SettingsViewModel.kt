package com.bale_bootcamp.guardiannews.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bale_bootcamp.guardiannews.GuardianNewsApp
import com.bale_bootcamp.guardiannews.data.local.datastore.SettingsDataStore
import com.bale_bootcamp.guardiannews.ui.settings.model.ColorTheme
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy
import com.bale_bootcamp.guardiannews.ui.settings.model.Setting
import com.bale_bootcamp.guardiannews.ui.settings.model.TextSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel (
    private val settingsDataStore: SettingsDataStore
): ViewModel() {
    private val _itemCount: MutableStateFlow<Int> = MutableStateFlow(10)
    val itemCount = _itemCount

    private val _orderBy: MutableStateFlow<OrderBy> = MutableStateFlow(OrderBy.NEWEST)
    val orderBy = _orderBy

    private val _fromDate: MutableStateFlow<String> = MutableStateFlow("")
    val fromDate = _fromDate

    private val _colorTheme: MutableStateFlow<ColorTheme> = MutableStateFlow(ColorTheme.WHITE)
    val colorTheme = _colorTheme

    private val _textSize: MutableStateFlow<TextSize> = MutableStateFlow(TextSize.MEDIUM)
    val textSize = _textSize

    init {
        viewModelScope.launch {
            settingsDataStore.apply {
                getItemCount().collect { itemCount ->
                    _itemCount.value = itemCount
                }
                getOrderBy().collect { orderBy ->
                    _orderBy.value = OrderBy.findByStr(orderBy)
                }
                getFromDate().collect { fromDate ->
                    _fromDate.value = fromDate
                }
                getColorTheme().collect { colorTheme ->
                    _colorTheme.value = ColorTheme.findByStr(colorTheme)
                }
                getFontSize().collect { fontSize ->
                    _textSize.value = TextSize.findByStr(fontSize)
                }
            }
        }
    }

    fun saveItemCount(itemCount: Int) = viewModelScope.launch {
        settingsDataStore.saveItemCount(itemCount)
    }

    fun saveOrderBy(orderBy: OrderBy) = viewModelScope.launch {
            settingsDataStore.saveOrderBy(orderBy.toString())
    }

    fun saveFromDate(fromDate: String) = viewModelScope.launch {
            settingsDataStore.saveFromDate(fromDate)
    }

    fun saveColorTheme(colorTheme: ColorTheme) = viewModelScope.launch {
            settingsDataStore.saveColorTheme(colorTheme.toString())
    }

    fun saveFontSize(fontSize: TextSize) = viewModelScope.launch {
            settingsDataStore.saveFontSize(fontSize.toString())
    }

    fun onSettingClicked(setting: Setting){
        //pass
    }

    class SettingsViewModelFactory: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                val settingsDataStore = SettingsDataStore(GuardianNewsApp.getAppContext())
                return SettingsViewModel(settingsDataStore) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
