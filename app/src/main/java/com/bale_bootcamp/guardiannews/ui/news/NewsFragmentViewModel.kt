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
import com.bale_bootcamp.guardiannews.data.network.NewsApi
import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import com.bale_bootcamp.guardiannews.data.repository.NewsRepository
import com.bale_bootcamp.guardiannews.data.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val TAG: String = "NewsFragmentViewModel"
class NewsFragmentViewModel (
    private val repository: NewsRepository
): ViewModel() {
    val news: MutableLiveData<PagingData<News>> = MutableLiveData()

    fun getNews(category: NewsApiService.Category, toDate: LocalDate) {
        viewModelScope.launch {
            repository.getNews(category, toDate).collectLatest {
                news.postValue(it)
                Log.d(TAG, "getNews: $it to date: $toDate")
            }
        }
    }

    fun refreshNews(category: NewsApiService.Category, toDate: LocalDate, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.refreshNews(category, toDate, page)
        }
    }


    class NewsFragmentViewModelFactory: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsFragmentViewModel::class.java)) {
                val appContext = GuardianNewsApp.getAppContext()
                val repository = NewsRepository(
                    NewsApi.retrofitApiService,
                    appContext.database.newsDao(),
                    SettingsRepository.getInstance(SettingsDataStore
                        .SettingsDataStoreFactory(appContext).create()))

                return NewsFragmentViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}