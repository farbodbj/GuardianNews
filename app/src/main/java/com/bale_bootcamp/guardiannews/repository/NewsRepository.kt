package com.bale_bootcamp.guardiannews.repository


import com.bale_bootcamp.guardiannews.model.ResponseModel
import com.bale_bootcamp.guardiannews.network.NewsApiService
import com.bale_bootcamp.guardiannews.onlinedatasources.NewsOnlineDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.time.LocalDate

class NewsRepository (
    private val onlineDataSource: NewsOnlineDataSource,
    private val lifeCycleAwareCoroutineScope: CoroutineScope
) {

    suspend fun getNews(category: NewsApiService.Category, fromDate: LocalDate, toDate: LocalDate, page: Int, pageSize: Int): ResponseModel? {
        return withContext(lifeCycleAwareCoroutineScope.coroutineContext) {
            onlineDataSource.getNews(category, fromDate, toDate, page, pageSize)
        }?.response
    }
}