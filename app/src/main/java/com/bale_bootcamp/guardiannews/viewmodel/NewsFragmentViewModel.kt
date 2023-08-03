package com.bale_bootcamp.guardiannews.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bale_bootcamp.guardiannews.GuardianNewsApp
import com.bale_bootcamp.guardiannews.model.News
import com.bale_bootcamp.guardiannews.network.NewsApi
import com.bale_bootcamp.guardiannews.network.NewsApiService
import com.bale_bootcamp.guardiannews.onlinedatasources.NewsOnlineDataSource
import com.bale_bootcamp.guardiannews.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate

class NewsFragmentViewModel (
    private val repository: NewsRepository
): ViewModel() {
    private val TAG: String = "NewsFragmentViewModel"

    fun getNews(category: NewsApiService.Category,
                fromDate: LocalDate,
                toDate: LocalDate,
                page: Int,
                pageSize: Int
    ): Flow<List<News>> = repository.getNews(category, fromDate, toDate, page, pageSize)


    fun refreshNews(category: NewsApiService.Category,
                    fromDate: LocalDate,
                    toDate: LocalDate,
                    page: Int,
                    pageSize: Int) {
        viewModelScope.launch{
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