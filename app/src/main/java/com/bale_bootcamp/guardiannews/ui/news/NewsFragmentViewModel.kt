package com.bale_bootcamp.guardiannews.ui.news


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.bale_bootcamp.guardiannews.GuardianNewsApp
import com.bale_bootcamp.guardiannews.data.local.datastore.SettingsDataStore
import com.bale_bootcamp.guardiannews.data.local.model.News
import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import com.bale_bootcamp.guardiannews.data.repository.NewsRepository
import com.bale_bootcamp.guardiannews.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private const val TAG: String = "NewsFragmentViewModel"
@HiltViewModel
class NewsFragmentViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {
    val news: MutableLiveData<PagingData<News>> = MutableLiveData()

    fun getNews(category: NewsApiService.Category, toDate: LocalDate, orderBy: OrderBy = OrderBy.RELEVANCE) {
        viewModelScope.launch {
            repository.getNews(category, toDate).collectLatest {
                news.postValue(it)
                Log.d(TAG, "getNews: $it to date: $toDate")
            }
        }
    }
}