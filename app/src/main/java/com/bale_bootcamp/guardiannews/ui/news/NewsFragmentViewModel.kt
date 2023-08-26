package com.bale_bootcamp.guardiannews.ui.news


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bale_bootcamp.guardiannews.GuardianNewsApp
import com.bale_bootcamp.guardiannews.data.local.datastore.SettingsDataStore
import com.bale_bootcamp.guardiannews.data.local.model.News
import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import com.bale_bootcamp.guardiannews.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private const val TAG: String = "NewsFragmentViewModel"
@HiltViewModel
class NewsFragmentViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {
    private var _news :Flow<PagingData<News>>? = null
    val news: Flow<PagingData<News>> get() = _news ?: emptyFlow()

    fun getNews(category: NewsApiService.Category, toDate: LocalDate, orderBy: OrderBy = OrderBy.RELEVANCE) {
        viewModelScope.launch {
            Log.d(TAG, "calling repository.getNews() for category: $category")
            _news = repository.getNews(category).cachedIn(this@launch).flowOn(Dispatchers.IO)
        }
    }
}