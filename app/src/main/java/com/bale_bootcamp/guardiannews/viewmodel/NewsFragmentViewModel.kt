package com.bale_bootcamp.guardiannews.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bale_bootcamp.guardiannews.GuardianNewsApp
import com.bale_bootcamp.guardiannews.model.News
import com.bale_bootcamp.guardiannews.network.NewsApi
import com.bale_bootcamp.guardiannews.network.NewsApiService
import com.bale_bootcamp.guardiannews.onlinedatasources.NewsOnlineDataSource
import com.bale_bootcamp.guardiannews.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class NewsFragmentViewModel (
    private val repository: NewsRepository
): ViewModel() {
    private val TAG: String = "NewsFragmentViewModel"

    val news: MutableLiveData<List<News>> = MutableLiveData()

    fun getNews(category: NewsApiService.Category,
                fromDate: LocalDate,
                toDate: LocalDate,
                page: Int,
                pageSize: Int) {
        viewModelScope.launch {
            repository.getNews(category, fromDate, toDate, page, pageSize).collect {
                news.postValue(it)
                Log.d(TAG, "getNews: $it")
            }
        }
    }

    fun refreshNews(category: NewsApiService.Category,
                    fromDate: LocalDate,
                    toDate: LocalDate,
                    page: Int,
                    pageSize: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.refreshNews(category, fromDate, toDate, page, pageSize)
        }
    }


    class NewsFragmentViewModelFactory: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsFragmentViewModel::class.java)) {
                val repository
                    = NewsRepository(
                        NewsOnlineDataSource(NewsApi.retrofitApiService),
                        GuardianNewsApp.getAppContext().database.newsDao()
                    )
                return NewsFragmentViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}