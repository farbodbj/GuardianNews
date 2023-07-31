package com.bale_bootcamp.guardiannews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bale_bootcamp.guardiannews.model.ResponseModel
import com.bale_bootcamp.guardiannews.network.NewsApi
import com.bale_bootcamp.guardiannews.network.NewsApiService
import com.bale_bootcamp.guardiannews.onlinedatasources.NewsOnlineDataSource
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate

class NewsRepository (
    private val onlineDataSource: NewsOnlineDataSource,
    private val lifeCycleAwareCoroutineScope: CoroutineScope
) {

    fun getNews(category: NewsApiService.Category,
        fromDate: LocalDate,
        toDate: LocalDate,
        page: Int,
        pageSize: Int
    ): MutableLiveData<ResponseModel?> = onlineDataSource.getNews(category, fromDate, toDate, page, pageSize)
}