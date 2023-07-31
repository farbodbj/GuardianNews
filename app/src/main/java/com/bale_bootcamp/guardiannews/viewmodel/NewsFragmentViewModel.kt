package com.bale_bootcamp.guardiannews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import com.bale_bootcamp.guardiannews.model.ResponseModel
import com.bale_bootcamp.guardiannews.network.NewsApiService
import com.bale_bootcamp.guardiannews.repository.NewsRepository
import java.time.LocalDate

class NewsFragmentViewModel (
    private val repository: NewsRepository
): ViewModel() {

    private val _category = MutableLiveData<NewsApiService.Category>()
    val category: LiveData<NewsApiService.Category> = _category

    private val _news = MutableLiveData<ResponseModel>()
    val news: LiveData<ResponseModel> = _news


    fun setCategory(category: NewsApiService.Category) {
        _category.value = category
    }

    suspend fun setNews(fromDate: LocalDate, toDate: LocalDate, page: Int, pageSize: Int) {
        _news.value = repository.getNews(category.value!!, fromDate, toDate, page, pageSize)
    }


    class NewsFragmentViewModelFactory(private val repository: NewsRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsFragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewsFragmentViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}