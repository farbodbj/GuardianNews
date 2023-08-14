package com.bale_bootcamp.guardiannews.ui.settings

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bale_bootcamp.guardiannews.data.local.datastore.SettingsDataStore
import com.bale_bootcamp.guardiannews.data.repository.SettingsRepository
import com.bale_bootcamp.guardiannews.ui.settings.model.ColorTheme
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy
import com.bale_bootcamp.guardiannews.ui.settings.model.TextSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SettingsViewModel"
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
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
        initializeFlows()
    }

    fun saveItemCount(itemCount: Int) = viewModelScope.launch {
        settingsRepository.saveItemCount(itemCount)
    }

    fun saveOrderBy(orderBy: OrderBy) = viewModelScope.launch {
            settingsRepository.saveOrderBy(orderBy.toString())
    }

    fun saveFromDate(fromDate: String) = viewModelScope.launch {
            settingsRepository.saveFromDate(fromDate)
    }

    fun saveColorTheme(colorTheme: ColorTheme) = viewModelScope.launch {
        settingsRepository.saveColorTheme(colorTheme.toString())
    }

    fun saveFontSize(fontSize: TextSize) = viewModelScope.launch {
            settingsRepository.saveFontSize(fontSize.toString())
    }


    private fun initializeFlows() {
        viewModelScope.launch {
            settingsRepository.apply {
                launch {
                    getItemCount().collect { itemCount ->
                        _itemCount.value = itemCount
                    }
                }
                launch {
                    getOrderBy().collect { orderBy ->
                        _orderBy.value = OrderBy.findByStr(orderBy)
                    }
                }
                launch {
                    getFromDate().collect { fromDate ->
                        _fromDate.value = fromDate
                    }
                }
                launch {
                    getColorTheme().collect { colorTheme ->
                        _colorTheme.value = ColorTheme.findByStr(colorTheme)
                    }
                }
                launch {
                    getFontSize().collect { fontSize ->
                        _textSize.value = TextSize.findByStr(fontSize.lowercase())
                    }
                }
            }
        }
    }

    class SettingsViewModelFactory(private val context: Context): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                val settingsDataStore = SettingsDataStore.SettingsDataStoreFactory(context).create()
                val settingsRepository = SettingsRepository.getInstance(settingsDataStore)
                return SettingsViewModel(settingsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
