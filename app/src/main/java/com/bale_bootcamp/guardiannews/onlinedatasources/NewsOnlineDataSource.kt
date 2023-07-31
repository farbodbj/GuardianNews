package com.bale_bootcamp.guardiannews.onlinedatasources

import com.bale_bootcamp.guardiannews.model.NetworkResponse
import com.bale_bootcamp.guardiannews.network.NewsApi
import com.bale_bootcamp.guardiannews.network.NewsApiService
import java.time.LocalDate

class NewsOnlineDataSource (
    private val apiService: NewsApiService = NewsApi.retrofitApiService
) {
    suspend fun getNews(category: NewsApiService.Category, fromDate: LocalDate, toDate: LocalDate, page: Int, pageSize: Int): NetworkResponse? {
        return apiService.getLatestFromCategory(category, fromDate, toDate, page, pageSize)
    }
}
