package com.bale_bootcamp.guardiannews.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bale_bootcamp.guardiannews.data.repository.SettingsRepository
import com.bale_bootcamp.guardiannews.ui.settings.model.ColorTheme
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy
import com.bale_bootcamp.guardiannews.ui.settings.model.TextSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SettingsViewModel"
@HiltViewModel
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
}
