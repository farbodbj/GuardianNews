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

    fun getNews(category: NewsApiService.Category,
                fromDate: LocalDate,
                toDate: LocalDate,
                page: Int,
                pageSize: Int): LiveData<ResponseModel?> {
        return repository.getNews(category, fromDate, toDate, page, pageSize)
    }


    class NewsFragmentViewModelFactory(): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsFragmentViewModel::class.java)) {
                val repository = NewsRepository((NewsOnlineDataSource(NewsApi.retrofitApiService)))
                return NewsFragmentViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}