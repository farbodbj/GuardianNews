package com.bale_bootcamp.guardiannews.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import com.bale_bootcamp.guardiannews.model.ResponseModel
import com.bale_bootcamp.guardiannews.network.NewsApi
import com.bale_bootcamp.guardiannews.network.NewsApiService
import com.bale_bootcamp.guardiannews.onlinedatasources.NewsOnlineDataSource
import com.bale_bootcamp.guardiannews.repository.NewsRepository
import java.time.LocalDate

class NewsFragmentViewModel (
    private val repository: NewsRepository
): ViewModel() {
    private val TAG: String = "NewsFragmentViewModel"

    private var _responseModel :MutableLiveData<ResponseModel?> = MutableLiveData(null)
    val responseModel: LiveData<ResponseModel?> = _responseModel

    fun refresh(category: NewsApiService.Category,
                fromDate: LocalDate,
                toDate: LocalDate,
                page: Int,
                pageSize: Int): LiveData<ResponseModel?> {
        Log.d(TAG, "${::refresh.name} called with values category: ${category.name}, fromDate: $fromDate, toDate: $toDate, page: $page, pageSize: $pageSize")

        // Just assigning new value returned by getNews() to _responseModel
        _responseModel.value = repository.getNews(category, fromDate, toDate, page, pageSize).value
        return _responseModel
    }

    fun getNews(category: NewsApiService.Category,
                fromDate: LocalDate,
                toDate: LocalDate,
                page: Int,
                pageSize: Int): LiveData<ResponseModel?> {
        return repository.getNews(category, fromDate, toDate, page, pageSize)
    }


    class NewsFragmentViewModelFactory(repositoryClass: Class<NewsRepository>): ViewModelProvider.Factory {
        private val repository = repositoryClass.constructors[0].newInstance(NewsOnlineDataSource(NewsApi.retrofitApiService)) as NewsRepository
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsFragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewsFragmentViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}